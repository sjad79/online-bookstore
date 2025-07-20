package com.myapp.bookstore.user;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/user")
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("/getAllUsers")
	public List<User> getUsers(){
		return userService.getUsers();
	}
	

	@GetMapping("/getUserByName/{name}")
	public User getUserByName(@RequestBody String name){
		return userService.getUserByName(name);
	}

	
	@GetMapping("/getUser/{userId}")
	public User getUserById(@PathVariable Long userId){
		return userService.getUserById(userId);
	}
	
	@PostMapping("/addUser")
	public void addNewUser(@RequestBody User User) {
		userService.addNewUser(User);
	}
	
	@DeleteMapping("/deleteUser/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
	}
	
    @PutMapping("/editUser/{id}")
    public void editUser(@PathVariable Long id, @RequestBody User updatedUser) {
        userService.editUser(id, updatedUser);
    }
	
}

