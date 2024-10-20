package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Publisher;
import com.example.goodreads.repository.BookJpaRepository;
import com.example.goodreads.repository.PublisherJpaRepository;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import com.example.goodreads.model.Author;
import java.util.NoSuchElementException;

@Service
public class BookJpaService implements BookRepository {

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Override
    public ArrayList<Book> getBooks() {
        List<Book> bookList = bookJpaRepository.findAll();
        ArrayList<Book> books = new ArrayList<>(bookList);
        return books;
    }

    @Override
    public Book getBookById(int bookId) {
        try {
            Book book = bookJpaRepository.findById(bookId).get();
            return book;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Book addBook(Book book) {
        try {
            int publisherId = book.getPublisher().getPublisherId();
            Publisher publisher = publisherJpaRepository.findById(publisherId).get();
            book.setPublisher(publisher);

            // Declaring an empty ArrayList of author IDs
            List<Integer> authorIds = new ArrayList<>();

            // Extract author IDs from the request object
            for (Author author : book.getAuthors()) {
                authorIds.add(author.getAuthorId());
            }

            // Retrieve the author entities from the database
            List<Author> authors = authorJpaRepository.findAllById(authorIds);

            // Validate all the author entities are retrieved from the database
            if (authors.size() != authorIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of authors are found");
            }

            // Map the authors to the book
            book.setAuthors(authors);

            // Save and return the book
            return bookJpaRepository.save(book);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong publisherId");
        }
    }

    @Override
    public Book updateBook(int bookId, Book book) {
        try {
            // Retrieve the existing book entity
            Book newBook = bookJpaRepository.findById(bookId).get();

            // Update all other fields of the book entity
            if (book.getName() != null) {
                newBook.setName(book.getName());
            }
            if (book.getImageUrl() != null) {
                newBook.setImageUrl(book.getImageUrl());
            }
            if (book.getPublisher() != null) {
                Publisher publisher = book.getPublisher();
                int publisherId = publisher.getPublisherId();
                Publisher newPublisher = publisherJpaRepository.findById(publisherId).get();
                newBook.setPublisher(newPublisher);
            }

            // Update authors along with the association
            if (book.getAuthors() != null) {
                // Extract author IDs from the request object
                List<Integer> authorIds = new ArrayList<>();
                for (Author author : book.getAuthors()) {
                    authorIds.add(author.getAuthorId());
                }

                // Retrieve and validate the author entities
                List<Author> authors = authorJpaRepository.findAllById(authorIds);
                if (authors.size() != authorIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some authors not found");
                }

                // Map the authors to the book
                newBook.setAuthors(authors);
            }
            // Save and return the book entity
            return bookJpaRepository.save(newBook);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong publisherId");
        }
    }

    @Override
    public void deleteBook(int bookId) {
        try {
            bookJpaRepository.deleteById(bookId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public Publisher getBookPublisher(int bookId) {
        try {
            Book book = bookJpaRepository.findById(bookId).get();
            return book.getPublisher();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Author> getBookAuthors(int bookId) {
        try {
            Book book = bookJpaRepository.findById(bookId).get();
            return book.getAuthors();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}