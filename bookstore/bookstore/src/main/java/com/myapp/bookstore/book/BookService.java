package com.myapp.bookstore.book;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bookstore.search.BookIndex;
import com.myapp.bookstore.search.BookIndexRepository;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final BookIndexRepository bookIndexRepository;  // For Elasticsearch
    private final CloudinaryService cloudinaryService;

    @Autowired
	public BookService(CloudinaryService cloudinaryService, BookRepository bookRepository, BookIndexRepository bookIndexRepository) {
		super();
	    this.cloudinaryService = cloudinaryService;
		this.bookRepository = bookRepository;
		this.bookIndexRepository = bookIndexRepository;

	}
	
	public List<Book> getBooks(){
			
		/*
		 * List<Book> books = bookRepository.findAll(); books.forEach(book ->
		 * bookIndexRepository.save(new BookIndex( book.getId(), book.getName(),
		 * book.getAuthor(), book.getPrice(), book.getParentOrganization(),
		 * book.getReleaseDate(), book.getPhotoUrl(), book.getGenres() )));
		 */
		 
		return bookRepository.findAll();
	}
	
	public void addNewBook(Book book) {
		System.out.println(book.getName() +  book.getAuthor());
		bookRepository.save(book);
		bookIndexRepository.save(new BookIndex(book.getId(), book.getName(), book.getAuthor(), book.getPrice(), book.getParentOrganization(), book.getReleaseDate(), book.getPhotoUrl(), book.getGenres())); // Index in Elasticsearch
		
	}
	
	public void deleteBook(Long id) {
		
        try {
            Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

            // ✅ Delete image from Cloudinary using CloudinaryService
            cloudinaryService.deleteImage(book.getPhotoUrl());

            // ✅ Delete book from database after image is removed
            bookRepository.deleteById(id);
            bookIndexRepository.deleteById(String.valueOf(id));  // ✅ Remove from Elasticsearch
        } catch (Exception e) {
            System.err.println("Error deleting book/image: " + e.getMessage());
            throw new RuntimeException("Failed to delete book or image", e);
        }


	}
	
    public void editBook(Long id, Book updatedBook) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        
        if (!bookOptional.isPresent()) {
            throw new IllegalStateException("Book with id " + id + " does not exist");
        }

        Book book = bookOptional.get();
        book.setName(updatedBook.getName());
        book.setAuthor(updatedBook.getAuthor());
        book.setGenres(updatedBook.getGenres());
        book.setPrice(updatedBook.getPrice());
        book.setParentOrganization(updatedBook.getParentOrganization());
        book.setReleaseDate(updatedBook.getReleaseDate());
        book.setPhotoUrl(updatedBook.getPhotoUrl());
        // Add any other fields that you want to update

        bookRepository.save(book);
        bookIndexRepository.save(new BookIndex(book.getId(), book.getName(), book.getAuthor(),
                                               book.getPrice(), book.getParentOrganization(),
                                               book.getReleaseDate(), book.getPhotoUrl(), book.getGenres())); 
        System.out.println("Book updated: " + book);
    }

	public Book getSingleBook(Long bookId) {
		// TODO Auto-generated method stub
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        
        if (!bookOptional.isPresent()) {
            throw new IllegalStateException("Book with id " + bookId + " does not exist");
        }

        Book book = bookOptional.get();
		return book;
	}
    
}
