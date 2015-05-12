package main.java.com.dawidrichert.database;

import main.java.com.dawidrichert.database.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BookTableModel extends AbstractTableModel {
    private List<Book> bookList;
    private String[] columnNames = { "Name", "Author", "Publisher", "Publication year" };

    public BookTableModel(List<Book> bookList) {
        this.bookList = bookList;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return bookList.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: return bookList.get(row).getName();
            case 1: return bookList.get(row).getAuthor();
            case 2: return bookList.get(row).getPublisher();
            case 3: return bookList.get(row).getPublicationYear();
        }
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
