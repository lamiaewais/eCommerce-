package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static com.example.demo.TestUtil.getModifyCartRequest;
import static com.example.demo.TestUtil.getUser;
import static org.junit.Assert.*;

public class CartControllerTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    private CartController cartController;

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtil.injectObjects(cartController, "userRepository", userRepository);
        TestUtil.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtil.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void when_non_existing_user_try_to_add_to_cart_not_found_status_should_be_returned() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_user_try_to_add_non_exiting_item_to_cart_not_found_status_should_be_returned() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(getUser());
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_existing_user_try_to_add_to_cart_ok_status_should_be_returned_and_body_should_not_be_null() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(TestUtil.getUser());
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(TestUtil.getItem()));

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void when_non_existing_user_try_to_remove_from_cart_not_found_status_should_be_returned() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_user_try_to_remove_non_exiting_item_from_cart_not_found_status_should_be_returned() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(getUser());
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        Mockito.verify(cartRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_existing_user_try_to_remove_from_cart_ok_status_should_be_returned_and_body_should_not_be_null() {
        Mockito.when(userRepository.findByUsername(getModifyCartRequest().getUsername())).thenReturn(TestUtil.getUser());
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(TestUtil.getItem()));

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequest());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        Mockito.verify(cartRepository, Mockito.times(1)).save(Mockito.any());
    }

}