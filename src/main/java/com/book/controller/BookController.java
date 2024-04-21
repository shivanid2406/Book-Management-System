package com.book.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		return findPageAndSort(0, "id", "asc", model);
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

	@GetMapping("/page/{pageNo}")
	public String findPageAndSort(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo, 3, sort);
		Page<Book> page = repo.findAll(pageable);
		List<Book> list = page.getContent();

		model.addAttribute("pageNo", pageNo);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("allBooks", list);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", sortDir.equals("asc") ? "desc" : "asc");
		return "list-books";

	}

}
