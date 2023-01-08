package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static com.example.demo.TestUtil.*;
import static org.junit.Assert.assertEquals;

public class OrderControllerTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private OrderController orderController;

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtil.injectObjects(orderController, "userRepository", userRepository);
        TestUtil.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void when_retrieving_orders_for_exiting_user_the_status_code_should_be_ok() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void when_retrieving_orders_for_non_exiting_user_the_status_code_should_be_not_found() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void when_existing_user_have_three_orders_the_response_body_should_hold_list_of_three_orders() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser());
        Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(getOrders());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).size(), getOrders().size());
    }

    @Test
    public void when_existing_user_have_no_orders_the_response_body_should_holds_empty_list() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser());
        Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).size(), 0);
    }

    @Test
    public void when_non_existing_user_try_to_submit_order_the_response_code_should_be_not_found() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
        Mockito.when(orderRepository.save(getUserOrder())).thenReturn(getUserOrder());

        ResponseEntity<UserOrder> response = orderController.submit(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void when_existing_user_try_to_submit_order_the_response_code_should_be_ok() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser());
        Mockito.when(orderRepository.save(getUserOrder())).thenReturn(getUserOrder());

        ResponseEntity<UserOrder> response = orderController.submit(getUser().getUsername());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}