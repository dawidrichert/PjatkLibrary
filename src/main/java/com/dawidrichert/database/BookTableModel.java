package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BookTableModel extends AbstractTableModel {

    private List<Book> books;
    private String[] columnNames = { "No.", "Name", "Author", "Publisher", "Year" };

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return books.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: return row+1;
            case 1: return books.get(row).getName();
            case 2: return books.get(row).getAuthor();
            case 3: return books.get(row).getPublisher();
            case 4: return books.get(row).getPublicationYear();
        }
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
