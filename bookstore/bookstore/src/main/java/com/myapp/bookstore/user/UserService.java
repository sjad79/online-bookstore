package com.myapp.bookstore.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // Injected PasswordEncoder

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Register a new user with hashed password
	public User addNewUser(User user) {
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
		return userRepository.save(user);
	}

	// Retrieve all users
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	// Delete a user by ID
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	// Edit a user's details and hash new password if updated
	public void editUser(Long id, User updatedUser) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exist"));

		user.setName(updatedUser.getName());
		user.setEmail(updatedUser.getEmail());
		user.setAddress(updatedUser.getAddress());

		// Hash the password if it's being updated
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		userRepository.save(user);
		System.out.println("User updated: " + user);
	}

	// Fetch user by username
	public User getUserByName(String name) {
		return userRepository.findByName(name);
	}
	
	// Fetch user by username
	public User getUserById(Long userId) {
		
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (!userOptional.isPresent()) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }

        User user = userOptional.get();
        
		return user;
	}
}