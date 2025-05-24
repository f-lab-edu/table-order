package com.flab.tableorder.service;

import com.flab.tableorder.domain.Call;
import com.flab.tableorder.domain.CallRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.domain.OptionRepository;
import com.flab.tableorder.dto.OrderDTO;
import com.flab.tableorder.dto.OptionDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.PriceNotMatchedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final CallRepository callRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;

    @Transactional(readOnly = true)
    public void orderMenu(List<OrderDTO> orderList, String storeId, String tableId) {
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
                .orElse(Collections.emptyList())
                .stream())
            .map(optionDTO -> new ObjectId(optionDTO.getOptionId()))
            .distinct()
            .toList();

        Map<ObjectId, Integer> optionPriceMap = orderList.stream()
            .flatMap(orderDTO -> Optional.ofNullable(orderDTO.getOptions())
                .orElse(Collections.emptyList())
                .stream())
            .collect(Collectors.toMap(
                optionDTO -> new ObjectId(optionDTO.getOptionId()),
                optionDTO -> optionDTO.getPrice()
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
        
//        TODO: 주문 수량과 가격을 redis에 저장
        int totalPrice = orderList.stream()
            .mapToInt(orderDTO -> orderDTO.getPrice() * orderDTO.getQuantity() +
                Optional.ofNullable(orderDTO.getOptions())
                    .orElse(Collections.emptyList())
                    .stream()
                    .mapToInt(optionDTO -> optionDTO.getPrice() * optionDTO.getQuantity())
                    .sum()

            )
            .sum();
//        TODO: POS기로 주문 정보 전송
    }

    @Transactional
    public void orderCall(List<String> callList, String storeId, String tableId) {
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
