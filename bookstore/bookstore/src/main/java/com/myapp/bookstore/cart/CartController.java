package com.myapp.bookstore.cart;

import java.util.List;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.user.User;


@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository; // ✅ Inject CartRepository


    @GetMapping("/user/cart/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return cartService.getCartByUser(user);
    }
    
    @GetMapping("/user/cart/{userId}/books")
    public ResponseEntity<List<Book>> getBooksInCart(@PathVariable Long userId) {
        try {
            User user = new User();
            user.setId(userId);
            Cart userCart = cartRepository.findByUser(user); // ✅ Fetch the Cart
            if (userCart == null) {
                return ResponseEntity.status(404).body(null); // ✅ Handle case where no cart exists
            }

            List<Book> books = userCart.getBooks(); // ✅ Extract Books
            return ResponseEntity.ok(books); // ✅ Return books as JSON
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @Transactional
    @PutMapping("/user/cart/{userId}/books/{bookId}/{change}")
 
    public void updateBookQuantityInCart(@PathVariable Long userId,
                                         @PathVariable Long bookId,
                                         @PathVariable int change) {
        User user = new User();
        user.setId(userId);
        cartService.updateBookQuantity(user, bookId, change);
    }

    @DeleteMapping("/user/cart/{userId}/books/{bookId}")
    public void removeBookFromCart(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = new User();
        user.setId(userId);
        cartService.removeBookFromCart(user, bookId);
    }

    @GetMapping("/public/cart/items")
    public List<Book> getCartItemsFromCookie(@CookieValue(value = "cart", defaultValue = "") String cartCookie) {
        return cartService.getCartItemsFromCookie(cartCookie);
    }

    @PutMapping("/public/cart/items/{bookId}/{change}")
    public void updateBookQuantityInCookie(@PathVariable Long bookId,
                                           @PathVariable int change,
                                           @CookieValue(value = "cart", defaultValue = "") String cartCookie,
                                           HttpServletResponse response) {

        // ✅ Reject invalid quantity changes
        if (change != 1 && change != -1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String updatedCartCookie = cartService.updateBookQuantityInCookie(bookId, cartCookie, change);

        Cookie cookie = new Cookie("cart", updatedCartCookie);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue() +
            "; Path=/; HttpOnly; Secure; SameSite=None");
    }

    @DeleteMapping("/public/cart/items/{bookId}")
    public void removeBookFromCartInCookie(@PathVariable Long bookId, 
                                           @CookieValue(value = "cart", defaultValue = "") String cartCookie, 
                                           HttpServletResponse response) {
        String updatedCartCookie = cartService.removeBookFromCartInCookie(bookId, cartCookie);

        Cookie cookie = new Cookie("cart", updatedCartCookie);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        // ✅ Manually set SameSite=None via response headers
        response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + 
            "; Path=/; HttpOnly; Secure; SameSite=None");
    }
    
    @PostMapping("/transfer")
    public void transferCartToUser(
            @CookieValue(value = "cart", defaultValue = "") String cartCookie,
            @PathVariable Long userId,
            HttpServletResponse response) {

        // Retrieve books from the cookie
        List<Book> booksFromCookie = cartService.getCartItemsFromCookie(cartCookie);

        // Retrieve the user's existing cart
        User user = new User();
        user.setId(userId);
        Cart userCart = cartService.getCartByUser(user);

        // Add books from the cookie to the user's cart
        cartService.mergeBooksIntoUserCart(userCart, booksFromCookie);

        // Save the updated cart
        cartService.saveUserCart(userCart);

        // Clear the cart cookie
        Cookie clearedCookie = new Cookie("cart", "");
        clearedCookie.setMaxAge(0); // Expire the cookie
        response.addCookie(clearedCookie);
    }

}



