package com.myapp.bookstore.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
public class ImageUploadController {
	
	@Autowired
    private final CloudinaryService cloudinaryService;

    public ImageUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            System.out.println(imageUrl);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl)); // ✅ Send image URL to frontend
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Upload failed"));
        }
    }
}