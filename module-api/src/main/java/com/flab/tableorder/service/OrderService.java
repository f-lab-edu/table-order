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
        List<ObjectId> menuIds = new ArrayList<>();
        Map<ObjectId, Integer> menuPriceMap = new HashMap<>();
        List<ObjectId> optionIds = new ArrayList<>();
        Map<ObjectId, Integer> optionPriceMap = new HashMap<>();

        int totalPrice = 0;
        for (OrderDTO orderDTO : orderList) {
            ObjectId objMenuId = new ObjectId(orderDTO.getMenuId());
            int menuPrice = orderDTO.getPrice();

            menuIds.add(objMenuId);
            menuPriceMap.put(objMenuId, menuPrice);
            totalPrice += menuPrice * orderDTO.getQuantity();

            List<OptionDTO> optionList = Optional.ofNullable(orderDTO.getOptions())
                .orElse(Collections.emptyList());
            for (OptionDTO optionDTO : optionList) {
                ObjectId objOptionId = new ObjectId(optionDTO.getOptionId());
                int optionPrice = optionDTO.getPrice();

                optionIds.add(objOptionId);
                optionPriceMap.put(objOptionId, optionPrice);
                totalPrice += optionPrice * optionDTO.getQuantity();
            }
        }
        optionIds = optionIds.stream().distinct().toList();

        List<Menu> menuList = menuRepository.findAllByMenuIdIn(menuIds);
        if (menuList.size() != menuIds.size())
            throw new MenuNotFoundException("Not found order a menu");

        for (Menu menu : menuList) {
            ObjectId menuId = menu.getMenuId();
            if (menu.getPrice() != menuPriceMap.get(menuId))
                throw new PriceNotMatchedException("Menu price does not match.",
                    menuPriceMap.get(menuId),
                    menu.getPrice());
        }

        List<Option> optionList = optionRepository.findAllByOptionIdIn(optionIds);
        if (optionList.size() != optionIds.size())
            throw new MenuNotFoundException("Attempted to order a option item that does not exist.");

        for (Option option : optionList) {
            ObjectId optionId = option.getOptionId();

            if (option.getPrice() != optionPriceMap.get(optionId))
                throw new PriceNotMatchedException("Option price does not match.",
                    optionPriceMap.get(optionId),
                    option.getPrice());
        }
        
//        TODO: 주문 수량과 가격을 redis에 저장
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
