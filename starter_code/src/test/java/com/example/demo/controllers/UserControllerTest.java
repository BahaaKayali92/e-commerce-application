package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    public static final String USER_NAME = "username";
    public static final String USER_PASSWORD = "test12345";

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUser() {
        when(bCryptPasswordEncoder.encode(USER_PASSWORD)).thenReturn(USER_PASSWORD);
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(USER_PASSWORD);
        createUserRequest.setConfirmPassword(USER_PASSWORD);

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().getUsername(), USER_NAME);
        assertEquals(responseEntity.getBody().getPassword(), USER_PASSWORD);
    }

    @Test
    public void testFindUserByIdSuccess() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setPassword(USER_PASSWORD);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.findByUserName(USER_NAME);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getUsername(), USER_NAME);
    }

    @Test
    public void testFindUserByIdFail() {
        ResponseEntity<User> responseEntity = userController.findByUserName(USER_NAME);
        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testFindByIdSuccess() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setPassword(USER_PASSWORD);
        when(userRepository.findById(new Long(1).longValue())).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> responseEntity = userController.findById(new Long(1).longValue());
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getUsername(), USER_NAME);
    }
}
