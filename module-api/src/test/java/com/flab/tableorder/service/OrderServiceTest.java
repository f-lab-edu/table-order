package com.flab.tableorder.service;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Call;
import com.flab.tableorder.domain.CallRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.domain.OptionRepository;
import com.flab.tableorder.domain.Store;

import java.util.List;

import com.flab.tableorder.dto.OrderDTO;
import com.flab.tableorder.dto.OrderOptionDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.PriceNotMatchedException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations valueOperations;
    @Mock
    private ListOperations<String, Object> listOperations;
    @Mock
    private CallRepository callRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OptionRepository optionRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void order_NotFound_Menu() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId("681edb5be8f2f34d23ecf6b0");
        orderDTO.setPrice(1000);
        orderDTO.setQuantity(1);

        when(menuRepository.findAllByMenuIdIn(List.of(new ObjectId(orderDTO.getMenuId())))).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.orderMenu(List.of(orderDTO), storeId))
            .isInstanceOf(MenuNotFoundException.class)
            .hasMessage("Not found order a menu");
    }

    @Test
    void order_NotMatch_Menu_price() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();

        String menuId = "681edb5be8f2f34d23ecf6b0";

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId(menuId);
        orderDTO.setPrice(1000);
        orderDTO.setQuantity(1);

        Menu menu = new Menu();
        menu.setMenuId(new ObjectId(menuId));
        menu.setPrice(1200);
        when(menuRepository.findAllByMenuIdIn(List.of(new ObjectId(orderDTO.getMenuId()))))
            .thenReturn(List.of(menu));

        assertThatThrownBy(() -> orderService.orderMenu(List.of(orderDTO), storeId))
            .isInstanceOf(PriceNotMatchedException.class)
            .hasMessageStartingWith("Menu price does not match.");
    }

    @Test
    void order_NotMatch_Option_size() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();

        String menuId = "681edb5be8f2f34d23ecf6b0";

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId(menuId);
        orderDTO.setPrice(1000);
        orderDTO.setQuantity(1);

        OrderOptionDTO optionDTO = new OrderOptionDTO();
        optionDTO.setOptionId("681edb5be8f2f34d23ecf6b1");
        List<OrderOptionDTO> optionList = List.of(optionDTO);

        orderDTO.setOptions(optionList);

        List<OrderDTO> orderList = List.of(orderDTO);
        Menu menu = new Menu();
        menu.setMenuId(new ObjectId(menuId));
        menu.setPrice(1000);
        when(menuRepository.findAllByMenuIdIn(
            orderList.stream()
                .map(order -> new ObjectId(order.getMenuId()))
                .toList()
        )).thenReturn(List.of(menu));

        ;
        when(optionRepository.findAllByOptionIdIn(optionList.stream()
            .map(option -> new ObjectId(option.getOptionId()))
            .toList())).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.orderMenu(orderList, storeId))
            .isInstanceOf(MenuNotFoundException.class)
            .hasMessage("Attempted to order a option item that does not exist.");
    }

    @Test
    void order_NotMatch_Option_price() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();

        String menuId = "681edb5be8f2f34d23ecf6b0";
        String optionId = "681edb5be8f2f34d23ecf6b1";

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId(menuId);
        orderDTO.setPrice(1000);
        orderDTO.setQuantity(1);
        List<OrderDTO> orderList = List.of(orderDTO);

        OrderOptionDTO optionDTO = new OrderOptionDTO();
        optionDTO.setOptionId(optionId);
        optionDTO.setPrice(500);
        List<OrderOptionDTO> optionList = List.of(optionDTO);

        orderDTO.setOptions(optionList);

        Menu menu = new Menu();
        menu.setMenuId(new ObjectId(menuId));
        menu.setPrice(1000);

        List<ObjectId> menuIds = orderList.stream()
            .map(order -> new ObjectId(order.getMenuId()))
            .toList();
        when(menuRepository.findAllByMenuIdIn(menuIds)).thenReturn(List.of(menu));

        Option option = new Option();
        option.setOptionId(new ObjectId(optionId));
        option.setPrice(700);

        List<ObjectId> optionIds = optionList.stream()
            .map(opt -> new ObjectId(opt.getOptionId()))
            .toList();
        when(optionRepository.findAllByOptionIdIn(optionIds)).thenReturn(List.of(option));

        assertThatThrownBy(() -> orderService.orderMenu(orderList, storeId))
            .isInstanceOf(PriceNotMatchedException.class)
            .hasMessageStartingWith("Option price does not match.");
    }

    private List<OrderDTO> getOrderList() {
        String menuId = "681edb5be8f2f34d23ecf6b0";
        String optionId = "681edb5be8f2f34d23ecf6b1";

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId(menuId);
        orderDTO.setPrice(1000);
        orderDTO.setQuantity(1);

        OrderOptionDTO optionDTO = new OrderOptionDTO();
        optionDTO.setOptionId(optionId);
        optionDTO.setPrice(500);
        List<OrderOptionDTO> optionList = List.of(optionDTO);

        orderDTO.setOptions(optionList);
        return List.of(orderDTO);
    }

    @Test
    void order_Success() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();

        List<OrderDTO> orderList = getOrderList();
        String menuId = orderList.get(0).getMenuId();

        List<OrderOptionDTO> optionList = orderList.get(0).getOptions();
        String optionId = optionList.get(0).getOptionId();

        Menu menu = new Menu();
        menu.setMenuId(new ObjectId(menuId));
        menu.setPrice(1000);

        List<ObjectId> menuIds = orderList.stream()
            .map(order -> new ObjectId(order.getMenuId()))
            .toList();
        when(menuRepository.findAllByMenuIdIn(menuIds)).thenReturn(List.of(menu));

        Option option = new Option();
        option.setOptionId(new ObjectId(optionId));
        option.setPrice(500);

        List<ObjectId> optionIds = optionList.stream()
            .map(opt -> new ObjectId(opt.getOptionId()))
            .toList();
        when(optionRepository.findAllByOptionIdIn(optionIds)).thenReturn(List.of(option));

        assertDoesNotThrow(() -> orderService.orderMenu(orderList, storeId));
    }

    @Test
    void updateOrder_Success() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        String storeId = mockStore.getStoreId().toString();
        List<OrderDTO> orderList = getOrderList();

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        assertDoesNotThrow(() -> orderService.updateOrderList(orderList, storeId, 1));
    }

    @Test
    void call_NotMatch_call_size() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        ObjectId objectId = mockStore.getStoreId();
        String storeId = objectId.toString();
        String callId = "681edb5be8f2f34d23ecf6b9";

        when(callRepository.findAllByCallIdInAndStoreId(
            List.of(new ObjectId(callId)),
            objectId
        )).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.orderCall(List.of(callId), storeId, 1))
            .isInstanceOf(MenuNotFoundException.class)
            .hasMessage("Attempted to order a call item that does not exist.");
    }

    @Test
    void call_Success() {
        Store mockStore = DataLoader.getDataInfo("store", "pizza.json", Store.class);
        ObjectId objectId = mockStore.getStoreId();
        String storeId = objectId.toString();

        Call call = DataLoader.getDataList("call", "pizza.json", Call.class).get(0);

        when(callRepository.findAllByCallIdInAndStoreId(
            List.of(call.getCallId()),
            objectId
        )).thenReturn(List.of(call));

        assertDoesNotThrow(() -> orderService.orderCall(List.of(call.getCallId().toString()), storeId, 1));
    }
}
