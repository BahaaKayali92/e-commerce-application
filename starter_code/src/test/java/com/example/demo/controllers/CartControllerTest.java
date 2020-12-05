package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    public static final String USER_NAME = "username";
    public static final String ITEM_NAME = "itemname";
    public static final long ITEM_ID = 1;


    public static User user;
    public static Item item;
    public static Cart cart;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        user = new User();
        user.setUsername(USER_NAME);

        item = new Item();
        item.setId(ITEM_ID);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.ONE);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(items);

        user.setCart(cart);

    }

    @Test
    public void testAddToCart() {
        Optional<Item> optionalItem = Optional.of(item);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        when(itemRepository.findById(ITEM_ID)).thenReturn(optionalItem);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(cartResponseEntity.getBody().getUser().getUsername(), USER_NAME);
        assertEquals(cartResponseEntity.getBody().getItems().size(), 2);
    }

    @Test
    public void testRemoveFromCart() {
        Optional<Item> optionalItem = Optional.of(item);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        when(itemRepository.findById(ITEM_ID)).thenReturn(optionalItem);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(ITEM_ID);
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(cartResponseEntity.getBody().getUser().getUsername(), USER_NAME);
        assertEquals(cartResponseEntity.getBody().getItems().size(), 0);
    }
}
