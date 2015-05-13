package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbBook {

    private final static String tableName            = "Book";
    private final static String col_Id               = "_id";
    private final static String col_Name             = "name";
    private final static String col_Author           = "author";
    private final static String col_Publisher        = "publisher";
    private final static String col_PublicationYear  = "publicationYear";

    public static void createTable(Database database) {
        try {
            database.openConnection();
            Statement stmt = database.getConnection().createStatement();

            String sql;
            sql  = String.format ("CREATE TABLE IF NOT EXISTS %s (", tableName);
            sql += String.format ("'%s' INTEGER PRIMARY KEY AUTOINCREMENT, ", col_Id);
            sql += String.format ("'%s' TEXT NOT NULL, ", col_Name);
            sql += String.format ("'%s' TEXT NOT NULL, ", col_Author);
            sql += String.format ("'%s' TEXT NOT NULL, ", col_Publisher);
            sql += String.format ("'%s' TEXT NOT NULL", col_PublicationYear);
            sql += ")";

            stmt.executeUpdate(sql);
            stmt.close();
            database.getConnection().commit();
            database.closeConnection();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void insert(Database database, Book book) {
        try {
            database.openConnection();

            String sql;
            sql  = String.format ("INSERT INTO %s (", tableName);
            sql += String.format ("%s, ", col_Name);
            sql += String.format ("%s, ", col_Author);
            sql += String.format ("%s, ", col_Publisher);
            sql += String.format ("%s) VALUES (?, ?, ?, ?)", col_PublicationYear);

            PreparedStatement stmt = database.getConnection().prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getPublicationYear());

            stmt.executeUpdate();
            stmt.close();
            database.getConnection().commit();
            database.closeConnection();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static void update(Database database, Book book) {
        try {
            database.openConnection();

            String sql;
            sql  = String.format ("UPDATE %s SET ", tableName);
            sql += String.format ("%s=?, ", col_Name);
            sql += String.format ("%s=?, ", col_Author);
            sql += String.format ("%s=?, ", col_Publisher);
            sql += String.format ("%s=? ", col_PublicationYear);
            sql += String.format ("WHERE %s=?", col_Id);

            PreparedStatement stmt = database.getConnection().prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getPublicationYear());
            stmt.setInt(5, book.getId());

            stmt.executeUpdate();
            database.getConnection().commit();
            stmt.close();
            database.closeConnection();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Record updated successfully");
    }

    public static void delete(Database database, Book book) {
        try {
            database.openConnection();
            Statement stmt = database.getConnection().createStatement();

            String sql = String.format("DELETE FROM %s WHERE %s=%s", tableName, col_Id, book.getId());

            stmt.executeUpdate(sql);
            database.getConnection().commit();
            stmt.close();
            database.closeConnection();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Record removed successfully");
    }

    public static void deleteAll(Database database) {
        try {
            database.openConnection();
            Statement stmt = database.getConnection().createStatement();

            String sql = String.format("DELETE FROM %s", tableName);

            stmt.executeUpdate(sql);
            database.getConnection().commit();
            stmt.close();
            database.closeConnection();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Record removed successfully");
    }

    public static List<Book> getBooks(Database database) {
        List<Book> books = new ArrayList<>();
        ResultSet rs = null;
        Statement stmt = null;
        try {
            database.openConnection();
            stmt = database.getConnection().createStatement();

            String sql = String.format("SELECT * FROM %s", tableName);

            rs = stmt.executeQuery(sql);

            while ( rs.next() ) {
                Book book = new Book();

                book.setId(rs.getInt(1));
                book.setName(rs.getString(2));
                book.setAuthor(rs.getString(3));
                book.setPublisher(rs.getString(4));
                book.setPublicationYear(rs.getString(5));

                books.add(book);
            }

            database.getConnection().commit();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                database.closeConnection();
            } catch (SQLException e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
        }
        System.out.println("Records created successfully");
        return books;
    }
}
