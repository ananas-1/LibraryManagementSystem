package com.example.LibraryManagementSystem.repo;

import com.example.LibraryManagementSystem.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepo extends JpaRepository<BookEntity, Integer> {
    BookEntity findByIsbn(String isbn);


}
