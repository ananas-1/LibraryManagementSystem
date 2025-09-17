package com.example.LibraryManagementSystem.repo;

import com.example.LibraryManagementSystem.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepo extends JpaRepository<BookEntity, Integer> {
    BookEntity findByIsbn(String isbn);


}
