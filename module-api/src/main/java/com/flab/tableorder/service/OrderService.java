package com.flab.tableorder.service;

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
import java.util.Objects;
import java.util.Optional;

import com.flab.tableorder.exception.MenuNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
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
            if (menu.getPrice() != menuPriceMap.get(menu.getMenuId()))
                throw new MenuNotFoundException("Menu price does not match.");
        }

        List<Option> optionList = optionRepository.findAllByOptionIdIn(optionIds);
        if (optionList.size() != optionIds.size())
            throw new MenuNotFoundException("Attempted to order a option item that does not exist.");

        for (Option option : optionList) {
            if (option.getPrice() != menuPriceMap.get(option.getOptionId()))
                throw new MenuNotFoundException("Option price does not match.");
        }
        
//        TODO: 주문 수량과 가격을 redis에 저장
//        TODO: 주문 내역을 DB에 저장
    }
}
