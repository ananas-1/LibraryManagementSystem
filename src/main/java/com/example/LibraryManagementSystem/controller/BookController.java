package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.dto.BookDto;
import com.example.LibraryManagementSystem.model.entity.BookEntity;
import com.example.LibraryManagementSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Books")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/addBook")
    public String addBook(@RequestBody BookDto bookDto) {
        return bookService.addBook(bookDto);
    }

    @GetMapping
    public String showAllBooks(Model model) {
        List<BookEntity> bookEntities = bookService.findAllBooks();
        model.addAttribute("books", bookEntities);
        return "books";
    }

    @GetMapping("/{isbn}")
    public BookDto GetBookDetails(@PathVariable String isbn) {
        BookDto bookDto = bookService.findBookByISBN(isbn);
//        model.addAttribute("bookDetails", bookDto);
//        return "bookDetails";
        return bookDto;
    }


    @PostMapping("/deleteBook")
    public String deleteBook(@RequestBody BookDto bookDto) {
        bookService.deleteBook(bookDto);
        return "redirect:/Books";
    }


    @PostMapping("/borrowBook")
    public String borrowBook(@RequestBody BookDto bookDto) {
        return bookService.borrowBook(bookDto);
//        return "redirect:/Books";
    }

    @PostMapping("/returnBook")
    public String returnBook(@RequestBody BookDto bookDto) {
        bookService.returnBook(bookDto);
        return "redirect:/Books";
    }
}
