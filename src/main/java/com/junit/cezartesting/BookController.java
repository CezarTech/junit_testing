package com.junit.cezartesting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @GetMapping(value = "{id}")
    public Book getBookById(@PathVariable("id") Long id) {
        return bookRepository.findById(id).get();
    }
    @PostMapping
    public Book createBook(@RequestBody Book book) {
      return bookRepository.save(book);
    }

    @PutMapping(value="{id}")
    public Book updateBook(@PathVariable("id") Long id, @RequestBody Book book) throws Exception {
        if(book==null||book.getBookId()==null){
            throw new Exception("Book not found");
        }
        Optional<Book> bookOptional = bookRepository.findById(book.getBookId());
        if(bookOptional.isEmpty()){
            throw new Exception("Book not found");
        }
        Book existingbook=bookOptional.get();
        existingbook.setName(book.getName());
        existingbook.setSummary(book.getSummary());
        existingbook.setRating(book.getRating());
        return bookRepository.save(existingbook);
    }

    @DeleteMapping(value = "{id}")
    public void deleteBook(@PathVariable("id") Long id) throws Exception {
        if(bookRepository.findById(id).isEmpty()){
            throw new Exception("Book not found");
        }
        bookRepository.deleteById(id);
    }

}
