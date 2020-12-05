package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserOrder userOrder = spy(UserOrder.class);

    public static final String USER_NAME = "username";
    public static final String ITEM_NAME = "itemname";
    public static Item item;
    public static Cart cart;
    public static User user;
    public static UserOrder order;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        user = new User();
        user.setUsername(USER_NAME);

        item = new Item();
        item.setDescription("description");
        item.setName(ITEM_NAME);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        cart = new Cart();
        cart.setItems(items);
        cart.setUser(user);

        user.setCart(cart);

        order = new UserOrder();
        order.setUser(user);
        order.setItems(items);
    }

    @Test
    public void testSubmit() {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        doReturn(order).when(userOrder).createFromCartWrapper(cart);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(USER_NAME);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().getUser().getUsername(), USER_NAME);
        assertEquals(responseEntity.getBody().getItems().get(0).getName(), ITEM_NAME);
    }

    @Test
    public void testGetOrdersForUser() {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        ArrayList<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(order);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(USER_NAME);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().get(0).getUser().getUsername(), USER_NAME);
    }
}
