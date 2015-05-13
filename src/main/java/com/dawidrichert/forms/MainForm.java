package main.java.com.dawidrichert.forms;

import main.java.com.dawidrichert.database.BookTableModel;
import main.java.com.dawidrichert.database.Database;
import main.java.com.dawidrichert.database.model.Book;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame {

    private final static String windowTitle = "PJATK Library Project";
    private final static int windowMinWidth = 600;
    private final static int windowMinHeight = 400;

    private JButton buttonAdd;
    private JPanel rootPanel;
    private JButton buttonEdit;
    private JButton buttonRemove;
    private JTable mainTable;
    private JButton buttonClose;

    private List<Book> books;

    public MainForm() {
        super(windowTitle);
        initUI();
        updateDataTable();
    }

    public void updateDataTable() {
        books = Database.getInstance().getBooks();
        BookTableModel bookTableModel = new BookTableModel(books);
        mainTable.setModel(bookTableModel);

        mainTable.getColumn(bookTableModel.getColumnName(0)).setMaxWidth(30);
        mainTable.getColumn(bookTableModel.getColumnName(4)).setMaxWidth(50);
    }

    private void initUI() {
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(windowMinWidth, windowMinHeight));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initMainTable();
        initButtons();
        initMenuBar();

        try {
            setIconImage(ImageIO.read(new File("src/main/res/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMainTable() {
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mainTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point p = e.getPoint();
                    int rowNumber = mainTable.rowAtPoint(p);
                    ListSelectionModel model = mainTable.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });

        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItemAdd = new JMenuItem("Add");
        menuItemAdd.addActionListener(e -> onAdd());
        popupMenu.add(menuItemAdd);

        JMenuItem menuItemEdit = new JMenuItem("Edit");
        menuItemEdit.addActionListener(e -> onEdit());
        popupMenu.add(menuItemEdit);

        JMenuItem menuItemRemove = new JMenuItem("Remove");
        menuItemRemove.addActionListener(e -> onRemove());
        popupMenu.add(menuItemRemove);

        mainTable.setComponentPopupMenu(popupMenu);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuMain = new JMenu(windowTitle);
        menuMain.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuMain);

        JMenuItem menuItemAbout = new JMenuItem("About");
        menuItemAbout.setMnemonic(KeyEvent.VK_A);
        menuItemAbout.addActionListener(e -> onAbout());
        menuMain.add(menuItemAbout);

        menuMain.addSeparator();

        JMenuItem menuItemAddSampleData = new JMenuItem("Add sample data");
        menuItemAddSampleData.setMnemonic(KeyEvent.VK_D);
        menuItemAddSampleData.addActionListener(e -> onAddSampleData());
        menuMain.add(menuItemAddSampleData);

        JMenuItem menuItemRemoveAllData = new JMenuItem("Remove all data");
        menuItemRemoveAllData.setMnemonic(KeyEvent.VK_R);
        menuItemRemoveAllData.addActionListener(e -> onRemoveAllData());
        menuMain.add(menuItemRemoveAllData);

        menuMain.addSeparator();

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.setMnemonic(KeyEvent.VK_E);
        menuItemExit.addActionListener(e -> onClose());
        menuMain.add(menuItemExit);

        menuBar.add(menuMain);

        setJMenuBar(menuBar);
    }

    private void initButtons() {
        buttonAdd.addActionListener(e -> onAdd());
        buttonEdit.addActionListener(e -> onEdit());
        buttonRemove.addActionListener(e -> onRemove());
        buttonClose.addActionListener(e -> onClose());
    }

    private void onAdd() {
        BookForm bookForm = new BookForm(this);
        bookForm.setVisible(true);
    }

    private void onEdit() {
        int selectedRowIndex = mainTable.getSelectedRow();
        if(selectedRowIndex != -1) {
            Book selectedBook = books.get(selectedRowIndex);

            BookForm bookForm = new BookForm(this, selectedBook);
            bookForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select book on the list to edit.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        mainTable.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
    }

    private void onRemove() {
        int selectedRowIndex = mainTable.getSelectedRow();
        if(selectedRowIndex != -1) {
            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to remove selected book?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                Book selectedBook = books.get(selectedRowIndex);
                Database.getInstance().removeBook(selectedBook);
                updateDataTable();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select book on the list to remove.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onClose() {
        System.exit(0);
    }

    private void onAbout() {
        JOptionPane.showMessageDialog(null, "\nCreated by Dawid Richert (s12792)\nwww.dawidrichert.com\n\n", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAddSampleData() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Daughter of Time", "Josephine Tey", "Crime Writers' Association", "1951"));
        books.add(new Book("The Hobbit", "J. R. R. Tolkien", "George Allen & Unwin", "1937"));
        books.add(new Book("The Da Vinci Code", "Dan Brown", "Doubleday", "2003"));
        books.add(new Book("Watership Down", "Richard Adams", "Rex Collings", "1972"));
        books.add(new Book("Think and Grow Rich", "Napoleon Hill", "The Ralston Society", "1937"));
        books.add(new Book("A Tale Of Two Cities", "Charles Dickens", "Chapman & Hall", "1859"));
        books.add(new Book("And Then There Were None", "Agatha Christie", "Collins Crime Club", "1939"));

        for(Book book : books) {
            Database.getInstance().saveBook(book);
        }
        updateDataTable();
    }

    private void onRemoveAllData() {
        Database.getInstance().removeAll();
        updateDataTable();
    }
}