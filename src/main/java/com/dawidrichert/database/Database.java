package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;
import main.java.com.dawidrichert.utils.MessageBox;

import java.sql.*;
import java.util.List;

public final class Database {

    private final String databaseName = "PjatkLibrary.db";
    private Connection connection = null;

    private static Database instance;

    private Database() { }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
            initTables();
        }
        return instance;
    }

    public void saveBook(Book book) {
        if(book.isNew()) {
            DbBook.insert(instance, book);
        } else {
            DbBook.update(instance, book);
        }
    }
    public void removeAll() {
        DbBook.deleteAll(instance);
    }

    public void removeBook(Book book) {
        DbBook.delete(instance, book);
    }

    public List<Book> getBooks() {
         return DbBook.getBooks(instance);
    }

    protected PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = openConnection();
        return connection.prepareStatement(sql);
    }

    protected void executeUpdate(String query) throws SQLException {
        Connection connection = openConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        connection.commit();
        stmt.close();
        closeConnection();
    }

    protected void executePreparedUpdate(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeUpdate();
        connection.commit();
        preparedStatement.close();
        closeConnection();
    }

    protected Connection openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            connection.setAutoCommit(false);
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showError("Cannot connect to database, so the application cannot be started.");
            System.exit(0);
        }
        return connection;
    }

    protected void closeConnection() {
        try {
            connection.close();
        } catch(SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showWarning("Cannot disconnect database. For safety, restart the application.");
        }
    }

    private static void initTables() {
        DbBook.createTable(instance);
    }
}