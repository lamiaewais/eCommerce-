package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent()) {
			log.error("Exception: " + id + " not found");
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.of(user);
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			log.error("Exception: " + username + " not found");
			return ResponseEntity.notFound().build();
		}

		log.info("found user with username: " + username);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if (createUserRequest.getPassword().length() < 7) {
			log.error("Exception: User wasn't created because, Password length should be greater than or equal 7!");
			return ResponseEntity.badRequest().build();
		} else if ( !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("Exception: User wasn't created because,Passwords do not match!");
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		userRepository.save(user);
		log.info("User created successfully");
		return ResponseEntity.ok(user);
	}
	
}
