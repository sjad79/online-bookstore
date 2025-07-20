package com.myapp.bookstore.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class BookController {
	
	private final BookService bookService;
	
	@Autowired
	public BookController(BookService bookService) {
		super();
		this.bookService = bookService;
	}

    @GetMapping("/public/book")
	public List<Book> getBooks(){
		return bookService.getBooks();
	}
    
    @GetMapping("/public/book/{bookId}")
	public Book getSingleBook(@PathVariable("bookId") Long bookId){
		return bookService.getSingleBook(bookId);
	}
    
	
	@PostMapping("/admin/book")
	public void addNewBook(@RequestBody Book book) {
		bookService.addNewBook(book);
	}
	
	@DeleteMapping("/admin/book/{bookId}")
	public void deleteBook(@PathVariable("bookId") Long bookId) {
		bookService.deleteBook(bookId);
	}
	
    @PutMapping("/admin/book/{id}")
    public void editBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        bookService.editBook(id, updatedBook);
    }
}
