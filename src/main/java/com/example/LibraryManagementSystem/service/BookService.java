package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.dto.BookDto;
import com.example.LibraryManagementSystem.model.entity.BookEntity;
import com.example.LibraryManagementSystem.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;


    public List<BookEntity> findAllBooks() {
        return bookRepo.findAll();
    }

    public BookDto findBookByISBN(String isbn) {
        BookEntity bookEntity = bookRepo.findByIsbn(isbn);
        return new BookDto(bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn(), bookEntity.getPublicationYear(), bookEntity.getCopies());
    }

    public String addBook(BookDto bookDto) {
        BookEntity bookEntity = new BookEntity(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getIsbn(), bookDto.getPublicationYear(), bookDto.getCopies());
        bookRepo.save(bookEntity);
        return "Book has been added successfully";
    }

    public String deleteBook(BookDto bookDto) {
        BookEntity bookEntity = bookRepo.findByIsbn(bookDto.getIsbn());
        bookRepo.delete(bookEntity);
        return "Book has been deleted";
    }

    public String borrowBook(BookDto bookDto) {
        BookEntity bookEntity = bookRepo.findByIsbn(bookDto.getIsbn());
        if (bookEntity == null) {
            return "Book not found";
        }
        if (bookEntity.getCopies() <= 0) {
            return "There is no available copies";
        }
        bookEntity.setCopies(bookEntity.getCopies() - 1);
        bookRepo.save(bookEntity);
        return "The book has been borrowed";
    }

    public String returnBook(BookDto bookDto) {
        BookEntity bookEntity = bookRepo.findByIsbn(bookDto.getIsbn());
        if (bookEntity == null) {
            return "Book not found";
        }
        bookEntity.setCopies(bookEntity.getCopies() + 1);
        bookRepo.save(bookEntity);
        return "The book has been returned";
    }




}
