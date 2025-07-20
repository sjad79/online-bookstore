package com.myapp.bookstore.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table
public class Book {
	@Id
	@SequenceGenerator(
			name = "book_sequence",
			sequenceName = "book_sequence",
			allocationSize = 1)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "book_sequence"
			)	
	private Long id;
	
	private String name;
	private String author;
	@ElementCollection  // ✅ Allows storing list values in a separate table
	private List<String> genres;
	private BigDecimal price;
	private String parentOrganization;
	private LocalDate releaseDate;
	private String photoUrl;
	
	
	public Book() {
		super();
	}
	
	
	
		public Book(String name, String author, List<String> genres, BigDecimal price, String parentOrganization, LocalDate releaseDate, String photoUrl) {
			super();
			this.name = name;
			this.author = author;
			this.genres = genres;
			this.price = price;
			this.parentOrganization = parentOrganization;
			this.releaseDate = releaseDate;
			this.photoUrl = photoUrl;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public LocalDate getReleaseDate() {
			return releaseDate;
		}

		public void setReleaseDate(LocalDate releaseDate) {
			this.releaseDate = releaseDate;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}
		

		public List<String> getGenres() {
			return genres;
		}



		public void setGenres(List<String> genres) {
			this.genres = genres;
		}



		public BigDecimal getPrice() {
			return price;
		}


		public void setPrice(BigDecimal price) {
			this.price = price;
		}



		public String getParentOrganization() {
			return parentOrganization;
		}

		public void setParentOrganization(String parentOrganization) {
			this.parentOrganization = parentOrganization;
		}



		public String getPhotoUrl() {
			return photoUrl;
		}



		public void setPhotoUrl(String photoUrl) {
			this.photoUrl = photoUrl;
		}
		
		
	
}
