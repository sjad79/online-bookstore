package com.myapp.bookstore.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.book.BookRepository;
import com.myapp.bookstore.user.User;

@Service
public class CartService {
	@Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public void updateBookQuantity(User user, Long bookId, int change) {
        if (change != 1 && change != -1) {
            throw new IllegalArgumentException("Quantity change must be either +1 or -1");
        }

        Cart cart = getCartByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        List<Book> books = cart.getBooks();

        if (change == 1) {
            books.add(book);
        } else {
            // Remove one instance only
            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(bookId)) {
                    iterator.remove();
                    break; // ✅ Remove just one copy
                }
            }
        }

        updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    public void removeBookFromCart(User user, Long bookId) {
        Cart cart = getCartByUser(user);
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        // ✅ Remove all matching books by ID
        cart.getBooks().removeIf(b -> b.getId().equals(book.getId()));

        updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    private void updateTotalPrice(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Book book : cart.getBooks()) {
            totalPrice = totalPrice.add(book.getPrice());
        }
        cart.setTotalPrice(totalPrice);
    }

    public List<Book> getCartItemsFromCookie(String cartCookie) {
        List<Book> cartItems = new ArrayList<>();

        if (cartCookie == null || cartCookie.isEmpty()) {
            // Log and return an empty list
            System.out.println("Cart cookie is null or empty");
            return cartItems;
        }

        try {
            // Decode and process the cart cookie
            String decodedCookie = new String(Base64.getDecoder().decode(cartCookie));
            System.out.println("Decoded cart cookie: " + decodedCookie);

            String[] bookIds = decodedCookie.split(",");
            for (String bookId : bookIds) {
                try {
                    Long id = Long.parseLong(bookId.trim());
                    Book book = bookRepository.findById(id).orElse(null);
                    if (book != null) {
                        cartItems.add(book);
                    } else {
                        System.out.println("Book not found for ID: " + id);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid book ID format: " + bookId);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to decode cart cookie: " + e.getMessage());
        }

        return cartItems;
    }

    public String updateBookQuantityInCookie(Long bookId, String cartCookie, int change) {
        List<String> bookIds = new ArrayList<>();
        if (!cartCookie.isEmpty()) {
            String decoded = new String(Base64.getDecoder().decode(cartCookie));
            bookIds.addAll(Arrays.asList(decoded.split(",")));
        }

        String targetId = bookId.toString();

        if (change == 1) {
            // ✅ Add one occurrence
            bookIds.add(targetId);
        } else if (change == -1) {
            // ✅ Remove one occurrence if it exists
            bookIds.remove(targetId);
        }

        String updatedCart = String.join(",", bookIds);
        return Base64.getEncoder().encodeToString(updatedCart.getBytes());
    }
    
    public String removeBookFromCartInCookie(Long bookId, String cartCookie) {
        List<String> bookIds = new ArrayList<>();
        if (!cartCookie.isEmpty()) {
            String decodedCookie = new String(Base64.getDecoder().decode(cartCookie));
            bookIds.addAll(Arrays.asList(decodedCookie.split(",")));
        }

        // ✅ Remove all occurrences of the book ID
        bookIds.removeIf(id -> id.equals(bookId.toString()));

        String updatedCart = String.join(",", bookIds);
        return Base64.getEncoder().encodeToString(updatedCart.getBytes());
    }
    
    public void mergeBooksIntoUserCart(Cart userCart, List<Book> booksFromCookie) {
        if (userCart == null) {
            throw new IllegalArgumentException("User cart cannot be null");
        }

        if (booksFromCookie == null || booksFromCookie.isEmpty()) {
            System.out.println("No books found in the cookie to merge");
            return; // Exit if there are no books to merge
        }

        // Convert the user's existing cart items to a Set for quick lookup
        Set<Long> existingBookIds = userCart.getBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        // Filter out books from the cookie that are not already in the user's cart
        List<Book> booksToAdd = booksFromCookie.stream()
                .filter(book -> !existingBookIds.contains(book.getId()))
                .collect(Collectors.toList());

        if (booksToAdd.isEmpty()) {
            System.out.println("All books from the cookie are already in the user's cart");
        } else {
            // Add the new books to the user's cart
            userCart.getBooks().addAll(booksToAdd);
            System.out.println("Added books to user's cart: " + booksToAdd);
        }
    }

    public void saveUserCart(Cart userCart) {
        if (userCart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }

        // Ensure the books list in the cart is not null
        if (userCart.getBooks() == null) {
            userCart.setBooks(new ArrayList<>()); // Initialize an empty list if null
            System.out.println("Books list in user cart was null; initialized an empty list");
        }

        // Log the current state of the cart
        System.out.println("Saving user cart with the following books: " + userCart.getBooks());

        // Update the total price before saving the cart
        updateTotalPrice(userCart);

        // Save the cart to the repository
        cartRepository.save(userCart);
        System.out.println("User cart saved successfully");
    }

}




