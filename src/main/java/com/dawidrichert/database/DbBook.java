package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;
import main.java.com.dawidrichert.utils.MessageBox;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbBook {

    private final static String tableName            = "Book";
    private final static String col_Id               = "_id";
    private final static String col_Name             = "name";
    private final static String col_Author           = "author";
    private final static String col_Publisher        = "publisher";
    private final static String col_PublicationYear  = "publicationYear";

    protected static void createTable(Database database) {
        try {
            String sql;
            sql  = String.format("CREATE TABLE IF NOT EXISTS %s (", tableName);
            sql += String.format("'%s' INTEGER PRIMARY KEY AUTOINCREMENT, ", col_Id);
            sql += String.format("'%s' TEXT NOT NULL, ", col_Name);
            sql += String.format("'%s' TEXT NOT NULL, ", col_Author);
            sql += String.format("'%s' TEXT NOT NULL, ", col_Publisher);
            sql += String.format("'%s' TEXT NOT NULL)", col_PublicationYear);

            database.executeUpdate(sql);
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showError("Something was wrong with the database, so the application should be closed.");
            System.exit(0);
        }
    }

    protected static void insert(Database database, Book book) {
        try {
            String sql;
            sql  = String.format ("INSERT INTO %s (", tableName);
            sql += String.format ("%s, ", col_Name);
            sql += String.format ("%s, ", col_Author);
            sql += String.format ("%s, ", col_Publisher);
            sql += String.format ("%s) VALUES (?, ?, ?, ?)", col_PublicationYear);

            PreparedStatement stmt = database.prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getPublicationYear());

            database.executePreparedUpdate(stmt);
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showWarning("Cannot insert book to database. For safety, restart the application.");
        }
    }

    protected static void update(Database database, Book book) {
        try {
            String sql;
            sql  = String.format ("UPDATE %s SET ", tableName);
            sql += String.format ("%s=?, ", col_Name);
            sql += String.format ("%s=?, ", col_Author);
            sql += String.format ("%s=?, ", col_Publisher);
            sql += String.format ("%s=? ", col_PublicationYear);
            sql += String.format ("WHERE %s=?", col_Id);

            PreparedStatement stmt = database.prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getPublicationYear());
            stmt.setInt(5, book.getId());

            database.executePreparedUpdate(stmt);
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showWarning("Cannot update book in database. For safety, restart the application.");
        }
    }

    protected static void delete(Database database, Book book) {
        try {
            String sql = String.format("DELETE FROM %s WHERE %s=%s", tableName, col_Id, book.getId());
            database.executeUpdate(sql);
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            MessageBox.showWarning("Cannot delete book from database. For safety, restart the application.");
        }
    }

    protected static void deleteAll(Database database) {
        try {
            String sql = String.format("DELETE FROM %s", tableName);
            database.executeUpdate(sql);
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showWarning("Cannot delete all books from database. For safety, restart the application.");
        }
    }

    protected static List<Book> getBooks(Database database) {
        List<Book> books = new ArrayList<>();
        try {
            Connection connection = database.openConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Book book = new Book();

                book.setId(rs.getInt(1));
                book.setName(rs.getString(2));
                book.setAuthor(rs.getString(3));
                book.setPublisher(rs.getString(4));
                book.setPublicationYear(rs.getString(5));

                books.add(book);
            }
            rs.close();
            stmt.close();
            database.closeConnection();
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            MessageBox.showWarning("Cannot return books from database. For safety, restart the application.");
        }
        return books;
    }
}
