/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.dao;

import fr.sogeti.domain.Book;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fduneau
 */
public class BookDAO {

    private final Map<Integer, Book> books;

    public BookDAO() {
	books = new HashMap<>();
	books.put(1, new Book(1, "Le chien de baskeville", "S A. Conan Doyles"));
	books.put(2, new Book(2, "Une étude en rouge", "S A. Conan Doyles"));
    }

    /**
     * Returns all the elements. 
     * @return
     */
    public Map<Integer, Book> getAll() {
	return books;
    }

    /**
     * Returns a single element corresponding to the given id.
     * @param id
     * @return
     */
    public Book get(Integer id) {
	return books.get(id);
    }

    /**
     * Adds a new element.
     * @param book
     * @return
     */
    public Book save(Book book) {
	if (book.getId() == null) {
	    book.setId(nextId());
	}
	books.put(book.getId(), book);
	return book;
    }

    /**
     * Increments the id in the collection and then points to it.
     * @return
     */
    private Integer nextId() {
	return Collections.max(books.keySet()) + 1;
    }

    /**
     * Removes an element corresponding to the given id.
     * @param id
     */
    public void delete(Integer id) {
	books.remove(id);
    }

    /**
     * Updates the element corresponding to the given id.
     * @param book
     */
    public void update(Book book) {
	if (books.containsKey(book.getId())) {
	    books.put(book.getId(), book);
	}
    }

}
