package com.myapp.bookstore.admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	Admin findByUsername(String username);
}
