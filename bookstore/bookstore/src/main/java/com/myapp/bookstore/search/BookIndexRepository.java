package com.myapp.bookstore.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface BookIndexRepository extends ElasticsearchRepository<BookIndex, String> {
    List<BookIndex> findByName(String name);
    List<BookIndex> findAll();
}