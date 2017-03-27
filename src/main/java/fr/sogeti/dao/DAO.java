/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.sogeti.domain.Element;

/**
 *
 * @author fduneau
 */
public abstract class DAO<T extends Element> implements InterfaceDAO<T> {

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver" ;
    private static final String DB_URL = "jdbc:mariadb://10.226.159.191/library?user=pi&password=pi";

    protected Connection connection;

    public DAO() {

    }

    public void init() {

	try {
	    Class.forName(JDBC_DRIVER);
	    connection = DriverManager.getConnection(DB_URL);
	} catch (ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}
    }

   

}
