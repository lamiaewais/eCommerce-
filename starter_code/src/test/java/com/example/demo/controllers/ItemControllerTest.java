package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;

public class ItemControllerTest {
    private final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtil.injectObjects(itemController, "log", log);
        TestUtil.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void when_retrieving_non_existing_item_id_status_code_should_be_not_found() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void when_retrieving_existing_item_id_status_code_should_be_ok() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getItem()));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void when_retrieving_non_existing_item_name_status_code_should_be_not_found() {
        Mockito.when(itemRepository.findByName(getItem().getName())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName(getItem().getName());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void when_retrieving_existing_item_name_status_code_should_be_ok() {
        Mockito.when(itemRepository.findByName(getItem().getName())).thenReturn(Collections.singletonList(getItem()));

        ResponseEntity<List<Item>> response = itemController.getItemsByName(getItem().getName());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void when_retrieving_all_items_status_code_should_be_ok() {
        Mockito.when(itemRepository.findAll()).thenReturn(Collections.singletonList(getItem()));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Name");
        return item;
    }
}