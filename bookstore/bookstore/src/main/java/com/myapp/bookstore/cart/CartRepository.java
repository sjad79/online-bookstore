package com.myapp.bookstore.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.bookstore.user.User;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findByUser(User user);
}
