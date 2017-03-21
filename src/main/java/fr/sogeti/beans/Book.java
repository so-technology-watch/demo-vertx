/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.beans;

import java.io.Serializable;

/**
 *
 * @author fduneau
 */
public class Book implements Serializable{
    private String nom;
    private String auteur;

    public Book(String nom, String auteur) {
        this.nom = nom;
        this.auteur = auteur;
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
    
}
