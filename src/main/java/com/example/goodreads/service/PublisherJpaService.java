package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Publisher;
import com.example.goodreads.repository.PublisherJpaRepository;
import com.example.goodreads.repository.PublisherRepository;
import com.example.goodreads.repository.BookJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublisherJpaService implements PublisherRepository {

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Override
    public ArrayList<Publisher> getPublishers() {
        List<Publisher> publisherList = publisherJpaRepository.findAll();
        ArrayList<Publisher> publishers = new ArrayList<>(publisherList);
        return publishers;
    }

    @Override
    public Publisher getPublisherById(int publisherId) {
        try {
            Publisher publisher = publisherJpaRepository.findById(publisherId).get();
            return publisher;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Publisher addPublisher(Publisher publisher) {
        publisherJpaRepository.save(publisher);
        return publisher;
    }

    @Override
    public Publisher updatePublisher(int publisherId, Publisher publisher) {
        try {
            Publisher new_publisher = publisherJpaRepository.findById(publisherId).get();
            if (publisher.getPublisherName() != null)
                new_publisher.setPublisherName(publisher.getPublisherName());
            publisherJpaRepository.save(new_publisher);
            return new_publisher;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deletePublisher(int publisherId) {
        try {
            // Fetch the publisher entity
            Publisher publisher = publisherJpaRepository.findById(publisherId).get();

            // Fetch books associated with the publisher
            ArrayList<Book> books = bookJpaRepository.findByPublisher(publisher);

            // Iterate over the books and set their publisher reference to NULL
            for (Book book : books) {
                book.setPublisher(null);
            }

            // Save the updated books
            bookJpaRepository.saveAll(books);

            // Delete the publisher
            publisherJpaRepository.deleteById(publisherId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
