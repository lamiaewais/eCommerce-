package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Objects;
import java.util.Optional;
import static com.example.demo.TestUtil.getUser;
import static org.junit.Assert.*;

public class UserControllerTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private UserController userController;

    @Before
    public void setup() {
        userController = new UserController();
        TestUtil.injectObjects(userController, "userRepository", userRepository);
        TestUtil.injectObjects(userController, "cartRepository", cartRepository);
        TestUtil.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void when_user_created_successfully_status_code_should_be_ok_and_password_should_be_hashed() {
        CreateUserRequest request = new CreateUserRequest();
        Mockito.when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).getPassword(), "hashedPassword");
    }

    @Test
    public void when_password_length_is_less_than_7_status_code_should_be_bad_request_and_user_should_not_be_saved() {
        CreateUserRequest request = new CreateUserRequest();
        Mockito.when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        request.setUsername("test");
        request.setPassword("pass");
        request.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(request);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_passwords_do_not_match_status_code_should_be_bad_request_and_user_should_not_be_saved() {
        CreateUserRequest request = new CreateUserRequest();
        Mockito.when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        request.setUsername("test");
        request.setPassword("passwordTest");
        request.setConfirmPassword("passwordTest_1");

        final ResponseEntity<User> response = userController.createUser(request);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void when_username_found_successfully_status_code_should_be_ok_and_body_should_not_be_null() {
        Mockito.when(userRepository.findByUsername("Lamia")).thenReturn(getUser());

        final ResponseEntity<User> response = userController.findByUserName("Lamia");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    public void when_username_not_found_status_code_should_be_not_found_and_body_should_be_null() {
        Mockito.when(userRepository.findByUsername("Lamia")).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName("Lamia");

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNull(response.getBody());
    }

    @Test
    public void when_id_found_successfully_status_code_should_be_ok_and_body_should_not_be_null() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(getUser()));

        final ResponseEntity<User> response = userController.findById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    public void when_id_not_found_status_code_should_be_not_found_and_body_should_be_null() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        final ResponseEntity<User> response = userController.findById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNull(response.getBody());
    }

}