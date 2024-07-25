package com.example.bookapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.tracing.Tracer;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private final BookRepository bookRepository;

	private final Counter myCounter;
	private final Timer myTimer;

	// private final Tracer tracer;

	@Autowired
	public BookController(BookRepository bookRepository, MeterRegistry registry,
			Tracer tracer) {

		this.bookRepository = bookRepository;
		this.myCounter = Counter.builder("demo_create_book_counter")
				.description("計數器-新建立書本次數")
				.register(registry);

		this.myTimer = Timer.builder("demo_get_all_book_timer")
				.description("計時器-查詢全部書本所花時間")
				.register(registry);

		// this.tracer = tracer;
	}

	@GetMapping
	public List<Book> getAllBooks() {
		return myTimer.record(() -> bookRepository.findAll());
	}

	@PostMapping
	public Book createBook(@RequestBody Book book) {
		myCounter.increment();

		return bookRepository.save(book);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		return bookRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
		return bookRepository.findById(id)
				.map(book -> {
					book.setTitle(bookDetails.getTitle());
					book.setAuthor(bookDetails.getAuthor());
					return ResponseEntity.ok(bookRepository.save(book));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable Long id) {
		return bookRepository.findById(id)
				.map(book -> {
					bookRepository.delete(book);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
