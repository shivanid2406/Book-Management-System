package com.book.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.book.dao.BookRepository;
import com.book.entity.Book;

@Controller
public class BookController {

	@Autowired
	private BookRepository repo;

	public BookController(BookRepository bookRepository) {
		bookRepository = repo;
	}

	@GetMapping("/list")
	public String getBooks(Model model) {

		List<Book> book = repo.findAll();
		model.addAttribute("book", book);
		return "list-books";
	}

	@GetMapping("/addBook")
	public String addBooks(Model model) {

		Book theBook = new Book();
		model.addAttribute("book", theBook);
		return "book-form";

	}

	@PostMapping("/save")
	public String saveBook(@ModelAttribute("book") Book theBook) {

		repo.save(theBook);
		return "redirect:/list";
	}

	@GetMapping("/updateBook")
	public String updateBooks(@RequestParam("id") int id, Model model) {

		Optional<Book> theBook = repo.findById(id);
		model.addAttribute("book", theBook);
		return "book-form";
	}
	
	@GetMapping("/deleteBook")
	public String deleteBook(@RequestParam("id") int id) {
		
		repo.deleteById(id);
		return "redirect:/list";
	}

}
