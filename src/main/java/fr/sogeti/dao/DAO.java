/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.dao;

import java.util.Collections;
import java.util.Map;

import fr.sogeti.domain.Element;



/**
 *
 * @author fduneau
 */
public class DAO<T extends Element> {

    private final Map<Integer, T> elements;

    public DAO(Map<Integer,T> elements) {
	this.elements = elements;

    }

    /**
     * Returns all the elements.
     * 
     * @return
     */
    public Map<Integer, T> getAll() {

	return elements;
    }

    /**
     * Returns a single element corresponding to the given id.
     * 
     * @param id
     * @return
     */
    public T get(Integer id) {

	return elements.get(id);
    }

    /**
     * Adds a new element.
     * 
     * @param book
     * @return
     */
    public T save(T element) {

	if (element.getId() == null) {
	    element.setId(nextId());
	}
	elements.put(element.getId(), element);
	return element;
    }

    /**
     * Increments the id in the collection and then points to it.
     * 
     * @return
     */
    private Integer nextId() {

	return Collections.max(elements.keySet()) + 1;
    }

    /**
     * Removes an element corresponding to the given id.
     * 
     * @param id
     */
    public void delete(Integer id) {

	elements.remove(id);
    }

    /**
     * Updates the element corresponding to the given id.
     * 
     * @param book
     */
    public void update(T element) {

	if (elements.containsKey(element.getId())) {
	    elements.put(element.getId(), element);
	}
    }

}
