package com.flab.tableorder.service;

import com.flab.tableorder.document.Call;
import com.flab.tableorder.document.CallRepository;
import com.flab.tableorder.document.Menu;
import com.flab.tableorder.document.MenuRepository;
import com.flab.tableorder.document.Option;
import com.flab.tableorder.document.OptionRepository;
import com.flab.tableorder.document.Stat;
import com.flab.tableorder.document.StatRepository;
import com.flab.tableorder.dto.OrderDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.PriceNotMatchedException;
import com.flab.tableorder.util.RedisUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final CallRepository callRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;
    private final StatRepository statRepository;

    private final RedisTemplate<String, List<OrderDTO>> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    public List<OrderDTO> orderMenu(List<OrderDTO> orderList, String storeId, int tableNum) {
        this.validationPrice(orderList, storeId);

        List<OrderDTO> allOrderList = this.updateAndGetOrderCache(orderList, storeId, tableNum);

        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        this.appendOrderAndGetStats(orderList, new ObjectId(storeId), date);

        String key = RedisUtil.getRedisKey("totalPrice", date, storeId);
        this.updateAndGetTotalPrice(orderList, key);

        return allOrderList;
    }

    public boolean validationPrice(List<OrderDTO> orderList, String storeId) {
        if (orderList.isEmpty()) throw new MenuNotFoundException("주문하신 메뉴가 없습니다.");

        Map<ObjectId, Integer> menuPriceMap = orderList.stream()
            .collect(Collectors.toMap(
                orderDTO -> new ObjectId(orderDTO.getMenuId()),
                orderDTO -> orderDTO.getPrice()
            ));

        List<ObjectId> menuIds = menuPriceMap.keySet()
            .stream()
            .toList();

        Map<ObjectId, Integer> optionPriceMap = orderList.stream()
            .flatMap(orderDTO -> Optional.ofNullable(orderDTO.getOptions())
                .orElse(List.of())
                .stream())
            .collect(Collectors.toMap(
                orderOptionDTO -> new ObjectId(orderOptionDTO.getOptionId()),
                orderOptionDTO -> orderOptionDTO.getPrice()
            ));

        List<ObjectId> optionIds = optionPriceMap.keySet()
            .stream()
            .toList();

        List<Menu> menuList = menuRepository.findAllByMenuIdIn(menuIds);
        if (menuList.size() != menuIds.size())
            throw new MenuNotFoundException("주문한 메뉴를 찾을 수 없습니다.");

        menuList.stream()
            .filter(menu -> menu.getPrice() != menuPriceMap.get(menu.getMenuId()))
            .findFirst()
            .ifPresent(menu -> {
                throw new PriceNotMatchedException("주문한 메뉴의 가격이 변동되었습니다.",
                    menuPriceMap.get(menu.getMenuId()),
                    menu.getPrice());
            });

        List<Option> optionList = optionRepository.findAllByOptionIdIn(optionIds);
        if (optionList.size() != optionIds.size())
            throw new MenuNotFoundException("주문한 옵션을 찾을 수 없습니다.");

        optionList.stream()
            .filter(option -> option.getPrice() != optionPriceMap.get(option.getOptionId()))
            .findFirst()
            .ifPresent(option -> {
                throw new PriceNotMatchedException("주문한 옵션의 가격이 변동되었습니다.",
                    optionPriceMap.get(option.getOptionId()),
                    option.getPrice());
            });

        return true;
    }

    @Transactional
    public List<OrderDTO> updateAndGetOrderCache(List<OrderDTO> orderList, String storeId, int tableNum) {
        String key = RedisUtil.getRedisKey("order:", storeId, Integer.toString(tableNum));
        List<OrderDTO> allOrderList = redisTemplate.opsForValue().get(key);
        allOrderList.addAll(orderList);

        redisTemplate.opsForValue().set(key, allOrderList);

        return allOrderList;
    }

    @Transactional
    public int updateAndGetTotalPrice(List<OrderDTO> orderList, String key) {
        int totalPrice = orderList.stream()
            .mapToInt(orderDTO -> orderDTO.getPrice() * orderDTO.getQuantity() +
                Optional.ofNullable(orderDTO.getOptions())
                    .orElse(Collections.emptyList())
                    .stream()
                    .mapToInt(optionDTO -> optionDTO.getPrice() * optionDTO.getQuantity())
                    .sum()
            )
            .sum();

        stringRedisTemplate.opsForValue().increment(key, totalPrice);

        return Integer.parseInt(stringRedisTemplate.opsForValue().get(key).toString());
    }

    @Transactional
    public List<Stat> appendOrderAndGetStats(List<OrderDTO> orderList, ObjectId storeId, String date) {
        List<Stat> statList = orderList.stream()
            .flatMap(order -> {
                ObjectId menuId = new ObjectId(order.getMenuId());

                Stat orderStat = Stat.builder()
                    .storeId(storeId)
                    .menuId(menuId)
                    .date(date)
                    .price(order.getPrice())
                    .quantity(order.getQuantity())
                    .build();

                Stream<Stat> optionStats = order.getOptions() == null ? Stream.empty() :
                    order.getOptions().stream()
                        .map(orderOption -> Stat.builder()
                            .storeId(storeId)
                            .menuId(menuId)
                            .optionId(new ObjectId(orderOption.getOptionId()))
                            .date(date)
                            .price(orderOption.getPrice())
                            .quantity(orderOption.getQuantity())
                            .build());

                return Stream.concat(Stream.of(orderStat), optionStats);
            })
            .toList();
        statRepository.IncrementOrderCount(statList);

        return this.getTodayOrderStats(storeId, date);
    }

    @Transactional(readOnly = true)
    public List<Stat> getTodayOrderStats(ObjectId storeId, String date) {
        return statRepository.findAllOrderStats(storeId, date);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "order", key = "#storeId + ':' + #tableNum")
    public List<OrderDTO> getOrderList(String storeId, int tableNum) {
        log.debug("getOrderList 캐시에 데이터 없음... DB 조회");
        return List.of();
    }

    @Transactional(readOnly = true)
    public void orderCall(List<String> callList, String storeId, int tableNum) {
        List<Call> findCallList = callRepository.findAllByCallIdInAndStoreId(
            callList.stream()
                .map(str -> new ObjectId(str))
                .toList(),
            new ObjectId(storeId));

        if (findCallList.size() != callList.size())

            throw new MenuNotFoundException("Attempted to order a call item that does not exist.");
//        TODO: POS기로 주문 정보 전송

    }
}
