package main.java.com.dawidrichert.forms;

import main.java.com.dawidrichert.database.BookTableModel;
import main.java.com.dawidrichert.database.Database;
import main.java.com.dawidrichert.database.model.Book;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class MainForm extends JFrame implements TableModelListener {
    private JButton buttonAdd;
    private JPanel rootPanel;
    private JButton buttonEdit;
    private JButton buttonRemove;
    private JTable table1;
    private JButton buttonClose;

    private List<Book> books;

    public MainForm() {
        super("PJATK Library Project");
        setContentPane(rootPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        buttonAdd.addActionListener(e -> {
            BookForm bookForm = new BookForm(this);
            bookForm.setVisible(true);
        });
        buttonEdit.addActionListener(e -> {
            int selectedIndex = table1.getSelectedRow();
            BookForm bookForm = new BookForm(this, books.get(selectedIndex));
            bookForm.setVisible(true);
        });
        buttonClose.addActionListener(e -> System.exit(0));

        updateDataTable();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
    }

    public void updateDataTable() {
        books = Database.getInstance().getBooks();

        BookTableModel bookTableModel = new BookTableModel(books);
        bookTableModel.addTableModelListener(this);
        table1.setModel(bookTableModel);
    }
}
