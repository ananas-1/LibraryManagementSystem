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
        return new BookDto(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn(), bookEntity.getPublicationYear(), bookEntity.getCopies());
    }

    public String addBook(BookDto bookDto) {
        BookEntity bookEntity = new BookEntity(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getIsbn(), bookDto.getPublicationYear(), bookDto.getCopies());
        bookRepo.save(bookEntity);
        return "Book has been added successfully";
    }

    public String deleteBook(String isbn) {
        BookEntity bookEntity = bookRepo.findByIsbn(isbn);
        bookRepo.delete(bookEntity);
        return "Book has been deleted";
    }

    public String borrowBook(String isbn) {
        BookEntity bookEntity = bookRepo.findByIsbn(isbn);
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

    public String returnBook(String isbn) {
        BookEntity bookEntity = bookRepo.findByIsbn(isbn);
        if (bookEntity == null) {
            return "Book not found";
        }
        bookEntity.setCopies(bookEntity.getCopies() + 1);
        bookRepo.save(bookEntity);
        return "The book has been returned";
    }


    public BookDto findBookById(int id) {
        BookEntity bookEntity = bookRepo.findById(id).get();
        BookDto bookDto = new BookDto(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn(), bookEntity.getPublicationYear(), bookEntity.getCopies());
        return bookDto;
    }


    public BookDto updateBook(BookDto bookDto) {
        // 1. Validate: Check if book exists
        BookEntity existingBook = bookRepo.findById(bookDto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookDto.getId()));

        // 2. Optional: Check for ISBN uniqueness (if ISBN changed)
        if (!existingBook.getIsbn().equals(bookDto.getIsbn())) {
            if (bookRepo.findByIsbn(bookDto.getIsbn()) != null) {
                throw new RuntimeException("A book with ISBN " + bookDto.getIsbn() + " already exists!");
            }
        }

        // 3. Update fields
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setIsbn(bookDto.getIsbn());
        existingBook.setPublicationYear(bookDto.getPublicationYear());
        existingBook.setCopies(bookDto.getCopies());

        // 4. Save → this will UPDATE the existing row (because ID is not null)
        BookEntity updatedBook = bookRepo.save(existingBook);

        // 5. Convert back to DTO and return (optional, but good practice)
        return convertToDto(updatedBook);
    }

    // Helper method: Entity → DTO
    private BookDto convertToDto(BookEntity bookEntity) {
        BookDto dto = new BookDto();
        dto.setId(bookEntity.getId());
        dto.setTitle(bookEntity.getTitle());
        dto.setAuthor(bookEntity.getAuthor());
        dto.setIsbn(bookEntity.getIsbn());
        dto.setPublicationYear(bookEntity.getPublicationYear());
        dto.setCopies(bookEntity.getCopies());
        return dto;
    }
}
