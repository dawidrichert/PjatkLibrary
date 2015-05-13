package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class Database {

    private final String databaseName = "PjatkLibrary.db";
    private Connection connection = null;

    private static Database instance;

    private Database() {
    }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
            DbBook.createTable(instance);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void saveBook(Book book) {
        DbBook.insert(instance, book);
    }

    public void updateBook(Book book) {
        DbBook.update(instance, book);
    }

    public List<Book> getBooks() {
         return DbBook.getBooks(instance);
    }

    public void removeAll() {
        DbBook.deleteAll(instance);
    }

    public void removeBook(Book book) {
        DbBook.delete(instance, book);
    }

    protected void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            connection.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    protected void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
