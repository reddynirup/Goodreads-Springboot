package com.example.goodreads.service;

import com.example.goodreads.model.Author;
import com.example.goodreads.model.Book;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.BookJpaRepository;
import com.example.goodreads.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorJpaService implements AuthorRepository {

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Override
    public ArrayList<Author> getAuthors() {
        List<Author> authorList = authorJpaRepository.findAll();
        ArrayList<Author> authors = new ArrayList<>(authorList);
        return authors;
    }

    @Override
    public Author getAuthorById(int authorId) {
        try {
            Author author = authorJpaRepository.findById(authorId).get();
            return author;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Author addAuthor(Author author) {
        // Extract book IDs from the request object
        List<Integer> bookIds = new ArrayList<>();
        for (Book book : author.getBooks()) {
            bookIds.add(book.getId());
        }

        // Retrieve the book entities from the database
        List<Book> books = bookJpaRepository.findAllById(bookIds);

        // Map the books to the author
        author.setBooks(books);

        // Add the author to all the books
        for (Book book : books) {
            book.getAuthors().add(author);
        }

        // Save the author entity
        Author savedAuthor = authorJpaRepository.save(author);

        // Save all the book entities
        bookJpaRepository.saveAll(books);

        // Return the saved author
        return savedAuthor;
    }

    @Override
    public Author updateAuthor(int authorId, Author author) {
        try {
            Author newAuthor = authorJpaRepository.findById(authorId).get();
            if (author.getAuthorName() != null) {
                newAuthor.setAuthorName(author.getAuthorName());
            }
            if (author.getBooks() != null) {
                // Clear all the existing associations of the author
                List<Book> books = newAuthor.getBooks();
                for (Book book : books) {
                    book.getAuthors().remove(newAuthor);
                }
                bookJpaRepository.saveAll(books);

                // Extract book IDs of the new books of the author
                List<Integer> newBookIds = new ArrayList<>();
                for (Book book : author.getBooks()) {
                    newBookIds.add(book.getId());
                }

                // Fetch all the new books from the database
                List<Book> newBooks = bookJpaRepository.findAllById(newBookIds);

                // Add the new associations of the author
                for (Book book : newBooks) {
                    book.getAuthors().add(newAuthor);
                }
                bookJpaRepository.saveAll(newBooks);

                // Map the new books to the author
                newAuthor.setBooks(newBooks);
            }
            return authorJpaRepository.save(newAuthor);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteAuthor(int authorId) {
        try {
            // Fetch the author entity
            Author author = authorJpaRepository.findById(authorId).get();

            // Remove the associations
            List<Book> books = author.getBooks();
            for (Book book : books) {
                book.getAuthors().remove(author);
            }

            // Update the book entity after removing the association
            bookJpaRepository.saveAll(books);

            // Delete the author
            authorJpaRepository.deleteById(authorId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Book> getAuthorBooks(int authorId) {
        try {
            Author author = authorJpaRepository.findById(authorId).get();
            return author.getBooks();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}