package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    public static final String ITEM_NAME = "itemname";
    public static final long ITEM_ID = 1;

    public static Item item;
    public static ArrayList<Item> items;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        item = new Item();
        item.setId(ITEM_ID);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.ONE);

        items = new ArrayList<>();
        items.add(item);
    }

    @Test
    public void testGetItems() {
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().size(), items.size());
    }

    @Test
    public void testGetItemByName() {
        when(itemRepository.findByName(ITEM_NAME)).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(ITEM_NAME);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().size(), items.size());
    }

    @Test
    public void testGetItemById() {
        Optional<Item> optionalItem = Optional.of(item);
        when(itemRepository.findById(ITEM_ID)).thenReturn(optionalItem);

        ResponseEntity<Item> responseEntity = itemController.getItemById(ITEM_ID);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().getId(), optionalItem.get().getId());
        assertEquals(responseEntity.getBody().getName(), optionalItem.get().getName());
    }
}
