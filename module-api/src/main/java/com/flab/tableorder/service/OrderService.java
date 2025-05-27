package com.flab.tableorder.service;

import com.flab.tableorder.domain.Call;
import com.flab.tableorder.domain.CallRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.domain.OptionRepository;
import com.flab.tableorder.dto.OrderDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.PriceNotMatchedException;
import com.flab.tableorder.util.RedisUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
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
    private final RedisTemplate<String, OrderDTO> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional(readOnly = true)
    public void orderMenu(List<OrderDTO> orderList, String storeId) {
        List<ObjectId> menuIds = orderList.stream()
            .map(orderDTO -> new ObjectId(orderDTO.getMenuId()))
            .toList();

        Map<ObjectId, Integer> menuPriceMap = orderList.stream()
            .collect(Collectors.toMap(
                orderDTO -> new ObjectId(orderDTO.getMenuId()),
                orderDTO -> orderDTO.getPrice()
            ));

        List<ObjectId> optionIds = orderList.stream()
            .flatMap(orderDTO -> Optional.ofNullable(orderDTO.getOptions())
                .orElse(List.of())
                .stream())
            .map(orderOptionDTO -> new ObjectId(orderOptionDTO.getOptionId()))
            .distinct()
            .toList();

        Map<ObjectId, Integer> optionPriceMap = orderList.stream()
            .flatMap(orderDTO -> Optional.ofNullable(orderDTO.getOptions())
                .orElse(List.of())
                .stream())
            .collect(Collectors.toMap(
                orderOptionDTO -> new ObjectId(orderOptionDTO.getOptionId()),
                orderOptionDTO -> orderOptionDTO.getPrice()
            ));

        List<Menu> menuList = menuRepository.findAllByMenuIdIn(menuIds);
        if (menuList.size() != menuIds.size())
            throw new MenuNotFoundException("Not found order a menu");

        menuList.stream()
            .filter(menu -> menu.getPrice() != menuPriceMap.get(menu.getMenuId()))
            .findFirst()
            .ifPresent(menu -> {
                throw new PriceNotMatchedException("Menu price does not match.",
                    menuPriceMap.get(menu.getMenuId()),
                    menu.getPrice());
            });

        List<Option> optionList = optionRepository.findAllByOptionIdIn(optionIds);
        if (optionList.size() != optionIds.size())
            throw new MenuNotFoundException("Attempted to order a option item that does not exist.");

        optionList.stream()
            .filter(option -> option.getPrice() != optionPriceMap.get(option.getOptionId()))
            .findFirst()
            .ifPresent(option -> {
                throw new PriceNotMatchedException("Option price does not match.",
                    optionPriceMap.get(option.getOptionId()),
                    option.getPrice());
            });

//        TODO: POS기로 주문 정보 전송
    }

    @Transactional
    public void updateOrderList(List<OrderDTO> orderList, String storeId, int tableNum) {
        redisTemplate.opsForList().rightPushAll(
            RedisUtil.getRedisKey("order", storeId, String.valueOf(tableNum)),
            orderList);

        stringRedisTemplate.opsForValue().increment(
            RedisUtil.getRedisKey(
                "totalPrice",
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE),
                storeId
            ),
            orderList.stream()
                .mapToInt(orderDTO -> orderDTO.getPrice() * orderDTO.getQuantity() +
                    Optional.ofNullable(orderDTO.getOptions())
                        .orElse(List.of())
                        .stream()
                        .mapToInt(orderOptionDTO -> orderOptionDTO.getPrice() * orderOptionDTO.getQuantity())
                        .sum()
                )
                .sum());
    }

    @Transactional
    public List<OrderDTO> getOrderList(String storeId, int tableNum) {
        return redisTemplate.opsForList().range(
            RedisUtil.getRedisKey("order", storeId, String.valueOf(tableNum)),
            0,
            -1);
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
