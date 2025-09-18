package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.dto.BookDto;
import com.example.LibraryManagementSystem.model.entity.BookEntity;
import com.example.LibraryManagementSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookDto()); // empty DTO for form binding
        return "addBook"; // add-book.html
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute BookDto bookDto, Model model) {
        try {
            bookService.addBook(bookDto);
            return "redirect:/books?success=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("book", bookDto); // repopulate form
            return "addBook";
        }
    }

    @GetMapping
    public String showAllBooks(Model model) {
        List<BookEntity> bookEntities = bookService.findAllBooks();
        model.addAttribute("books", bookEntities);
        return "books";
    }

    @GetMapping("/{isbn}")
    public String GetBookDetails(@PathVariable String isbn, Model model) {
        BookDto bookDto = bookService.findBookByISBN(isbn);
        model.addAttribute("book", bookDto);
        return "bookDetails";
    }


    // ➤ Show Edit Book Form
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable int id, Model model) {
        BookDto bookDto = bookService.findBookById(id); // you may need to add this method in service
        if (bookDto == null) {
            return "redirect:/books?error=Book not found";
        }
        model.addAttribute("book", bookDto);
        return "editBook"; // edit-book.html
    }

    // ➤ Handle Edit Book Form Submission
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute BookDto bookDto, Model model) {
        try {
            bookDto.setId(id); // ensure ID is set
            bookService.updateBook(bookDto); // you may need to add update method
            return "redirect:/books?success=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("book", bookDto);
            return "editBook";
        }
    }

    // ➤ Delete Book (GET for simplicity, as per requirements)
    @GetMapping("/delete/{isbn}")
    public String deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn); // add this method in service
        return "redirect:/books?success=true";
    }

    // ➤ Borrow Book
    @GetMapping("/borrow/{isbn}")
    public String borrowBook(@PathVariable String isbn) {
        bookService.borrowBook(isbn); // add this method
        return "redirect:/books?success=Borrowed successfully";
    }

    // ➤ Return Book
    @GetMapping("/return/{isbn}")
    public String returnBook(@PathVariable String isbn) {
        bookService.returnBook(isbn); // add this method
        return "redirect:/books?success=Returned successfully";
    }
}
