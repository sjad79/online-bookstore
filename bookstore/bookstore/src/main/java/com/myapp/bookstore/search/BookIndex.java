package com.myapp.bookstore.search;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(indexName = "books_index")
public class BookIndex {
    @Id
    private String id;
    private String name;
    private String author;
    private BigDecimal price;
    private String parentOrganization;

    @Field(type = FieldType.Date, format = DateFormat.year_month_day)
    private LocalDate releaseDate;
    private String photoUrl;

    @Field(type = FieldType.Keyword)  // ✅ Correct annotation for Elasticsearch List<String>
    private List<String> genres;

    // Constructors
    public BookIndex() {}

    public BookIndex(Long id, String name, String author, BigDecimal price, String parentOrganization, LocalDate releaseDate, String photoUrl, List<String> genres) {
        this.id = id != null ? id.toString() : null;
        this.name = name;
        this.author = author;
        this.price = price;
        this.parentOrganization = parentOrganization;
        this.releaseDate = releaseDate;
        this.photoUrl = photoUrl;
        this.genres = genres; // ✅ Initialize genres field
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getParentOrganization() { return parentOrganization; }
    public void setParentOrganization(String parentOrganization) { this.parentOrganization = parentOrganization; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public List<String> getGenres() { return genres; }  // ✅ Getter for genres
    public void setGenres(List<String> genres) { this.genres = genres; }  // ✅ Setter for genres
}