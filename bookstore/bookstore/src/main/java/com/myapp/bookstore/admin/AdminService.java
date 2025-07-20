package com.myapp.bookstore.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.book.BookRepository;
import com.myapp.bookstore.user.User;
import com.myapp.bookstore.user.UserRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

        public void addBook(Book book) {
            bookRepository.save(book);
        }

        public void editBook(Long bookId, Book updatedBook) {
            Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
            book.setName(updatedBook.getName());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            book.setParentOrganization(updatedBook.getParentOrganization());
            book.setReleaseDate(updatedBook.getReleaseDate());
            bookRepository.save(book);
        }

        public void removeBook(Long bookId) {
            bookRepository.deleteById(bookId);
        }

        public void addUser(User user) {
            userRepository.save(user);
        }

        public void editUser(Long userId, User updatedUser) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(updatedUser.getName());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setAddress(updatedUser.getAddress());
            userRepository.save(user);
        }

        public void removeUser(Long userId) {
            userRepository.deleteById(userId);
        }
       
}
