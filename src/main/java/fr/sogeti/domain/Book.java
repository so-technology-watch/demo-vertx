/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.domain;

import java.io.Serializable;

/**
 *
 * @author fduneau
 */
public class Book implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1086896242188777309L;
    private Integer id;
    private String nom;
    private String auteur;

    public Book(Integer id, String nom, String auteur) {
	this.id = id;
	this.nom = nom;
	this.auteur = auteur;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getNom() {
	return nom;
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public String getAuteur() {
	return auteur;
    }

    public void setAuteur(String auteur) {
	this.auteur = auteur;
    }

    @Override
    public String toString() {
	return "Book{" + "id=" + id + ", nom=" + nom + ", auteur=" + auteur + '}';
    }

}
