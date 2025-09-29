package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.dto.BookDto;
import com.example.LibraryManagementSystem.model.entity.BookEntity;
import com.example.LibraryManagementSystem.model.entity.UserEntity;
import com.example.LibraryManagementSystem.service.BookService;
import jakarta.servlet.http.HttpSession;
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
    public String showAddBookForm(Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else if (!currentUser.getAdmin()){
            return "redirect:/books";
        }
        model.addAttribute("book", new BookDto()); // empty DTO for form binding
        return "addBook"; // add-book.html
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute BookDto bookDto, Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else if (!currentUser.getAdmin()){
            return "redirect:/books";
        }
        try {
            bookService.addBook(bookDto);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("book", bookDto); // repopulate form
            return "addBook";
        }
    }

    @GetMapping
    public String showAllBooks(Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<BookEntity> bookEntities = bookService.findAllBooks();
        model.addAttribute("books", bookEntities);
        return "books";
    }

    @GetMapping("/{isbn}")
    public String GetBookDetails(@PathVariable String isbn, Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        BookDto bookDto = bookService.findBookByISBN(isbn);
        model.addAttribute("book", bookDto);
        return "bookDetails";
    }


    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable int id, Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else if (!currentUser.getAdmin()){
            return "redirect:/books";
        }
        BookDto bookDto = bookService.findBookById(id);
        if (bookDto == null) {
            return "redirect:/books";
        }
        model.addAttribute("book", bookDto);
        return "editBook";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute BookDto bookDto, Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else if (!currentUser.getAdmin()){
            return "redirect:/books";
        }
        try {
            bookDto.setId(id);
            bookService.updateBook(bookDto);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("book", bookDto);
            return "editBook";
        }
    }

    @GetMapping("/delete/{isbn}")
    public String deleteBook(@PathVariable String isbn, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else if (!currentUser.getAdmin()){
            return "redirect:/books";
        }
        bookService.deleteBook(isbn);
        return "redirect:/books";
    }

    @GetMapping("/borrow/{isbn}")
    public String borrowBook(@PathVariable String isbn, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        bookService.borrowBook(isbn);
        return "redirect:/books";
    }

    @GetMapping("/return/{isbn}")
    public String returnBook(@PathVariable String isbn, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        bookService.returnBook(isbn); // add this method
        return "redirect:/books";
    }
}
