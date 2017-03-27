package fr.sogeti.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.sogeti.domain.Book;

public class BookDao extends DAO<Book> {

    @Override
    public Book get(int id) {

	Book resBook = null;
	String request = "SELECT * FROM book WHERE ID = " + id + ";";
	Statement statement;
	try {
	    statement = connection.createStatement();
	    ResultSet res = statement.executeQuery(request);
	    res.next();
	    int idElement = res.getInt("id");
	    String titre = res.getString("titre");
	    String auteur = res.getString("auteur");
	    resBook = new Book(idElement, titre, auteur);
	    res.close();
	    statement.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return resBook;
    }

    @Override
    public List<Book> getAll() {

	List<Book> books = new ArrayList<>();
	String request = "SELECT * FROM book;";
	Statement statement;
	try {
	    statement = connection.createStatement();
	    ResultSet res = statement.executeQuery(request);

	    while (res.next()) {
		int idElement = res.getInt("id");
		String titre = res.getString("titre");
		String auteur = res.getString("auteur");
		books.add(new Book(idElement, titre, auteur));
	    }

	    res.close();
	    statement.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return books;

    }

    @Override
    public void save(Book element) {

	String request = "INSERT INTO book (titre, auteur) VALUES ('" + element.getNom() + "', '" + element.getAuteur()
		+ "');";
	Statement statement;
	try {
	    statement = connection.createStatement();
	    statement.executeQuery(request);
	    statement.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    @Override
    public void update(Book book) {

	String request = "UPDATE book SET titre = '" + book.getNom() + "', auteur = '" + book.getAuteur()
		+ "' WHERE id = '" + book.getId() + "';";
	Statement statement;
	try {
	    statement = connection.createStatement();
	    statement.executeQuery(request);
	    statement.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void delete(int id) {

	String request = "DELETE * FROM book WHERE id = '" + id + "';";
	Statement statement;
	try {
	    statement = connection.createStatement();
	    statement.executeQuery(request);
	    statement.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
