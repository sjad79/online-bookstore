package com.myapp.bookstore.search;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class SearchController {

    @Autowired
    private BookIndexRepository bookIndexRepository; // Elasticsearch Repository

    @GetMapping("/search")
    public List<BookIndex> searchBooks(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) List<String> genres // ✅ Accept list of genres
    ) {
        List<BookIndex> results;

        if (query == null || query.isEmpty()) {    
            results = new ArrayList<>(bookIndexRepository.findAll());  // ✅ Fetch all books
        } else {
            results = new ArrayList<>(bookIndexRepository.findByName(query));  // ✅ Search by name
        }

        System.out.println("Search Query: " + query + " | Sorting: " + sort + " | Genres: " + genres);

     // ✅ Apply genre filtering
        if (genres != null && !genres.isEmpty()) {
            results = results.stream()
                .filter(book -> genres.stream().allMatch(book.getGenres()::contains)) // ✅ Book must contain ALL selected genres
                .collect(Collectors.toList());
        }


        // ✅ Apply sorting filters  
        if ("price_asc".equals(sort)) {
            results.sort(Comparator.comparing(BookIndex::getPrice));
        } else if ("price_desc".equals(sort)) {
            results.sort(Comparator.comparing(BookIndex::getPrice).reversed());
        } else if ("date_desc".equals(sort)) {
            results.sort(Comparator.comparing(BookIndex::getReleaseDate).reversed());
        }
        
        System.out.println(results);
        return results;
    }

}