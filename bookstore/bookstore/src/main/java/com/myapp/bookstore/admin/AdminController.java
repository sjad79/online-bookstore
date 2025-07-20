package com.myapp.bookstore.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.user.User;

@RestController
@RequestMapping("api/admin")
public class AdminController {
	@Autowired
    private AdminService adminService;

    @PostMapping("/books")
    public void addBook(@RequestBody Book book) {
        adminService.addBook(book);
    }

    @PutMapping("/books/{id}")
    public void editBook(@PathVariable Long id, @RequestBody Book book) {
        adminService.editBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void removeBook(@PathVariable Long id) {
        adminService.removeBook(id);
    }

    @PostMapping("/users")
    public void addUser(@RequestBody User user) {
        adminService.addUser(user);
    }

    @PutMapping("/users/{id}")
    public void editUser(@PathVariable Long id, @RequestBody User user) {
        adminService.editUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void removeUser(@PathVariable Long id) {
        adminService.removeUser(id);
    }
    
}

