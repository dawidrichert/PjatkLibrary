package com.dawidrichert.forms;

import com.dawidrichert.database.BookTableModel;
import com.dawidrichert.database.Database;
import com.dawidrichert.database.model.Book;
import com.dawidrichert.utils.MessageBox;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private BookTableModel bookTableModel;

    public MainForm() {
        super(windowTitle);
        initUI();
        updateDataTable();
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
            setIconImage(ImageIO.read(new File("src/main/resources/icon.png")));
        } catch (IOException e) {
            System.err.println("Failed to set icon for the application. " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void updateDataTable() {
        books = Database.getInstance().getBooks();
        bookTableModel = new BookTableModel(books);
        mainTable.setModel(bookTableModel);
        setColumnsSizes();
    }

    private void setColumnsSizes() {
        mainTable.getColumn(bookTableModel.getColumnName(0)).setMaxWidth(30);
        mainTable.getColumn(bookTableModel.getColumnName(4)).setMaxWidth(50);
    }

    private void initMainTable() {
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mainTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    int rowIndex = mainTable.rowAtPoint(e.getPoint());
                    mainTable.setRowSelectionInterval(rowIndex, rowIndex);
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
        if (selectedRowIndex != -1) {
            Book selectedBook = books.get(selectedRowIndex);

            BookForm bookForm = new BookForm(this, selectedBook);
            bookForm.setVisible(true);
            mainTable.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
        } else {
            MessageBox.showInformation("Please select book on the list to edit.");
        }
    }

    private void onRemove() {
        int selectedRowIndex = mainTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to remove selected book?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                Book selectedBook = books.get(selectedRowIndex);
                Database.getInstance().removeBook(selectedBook);
                updateDataTable();
            }
        } else {
            MessageBox.showInformation("Please select book on the list to remove.");
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

        for (Book book : books) {
            Database.getInstance().saveBook(book);
        }
        updateDataTable();
    }

    private void onRemoveAllData() {
        Database.getInstance().removeAll();
        updateDataTable();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(15, 15, 15, 15), -1, -1));
        rootPanel.setPreferredSize(new Dimension(800, 600));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAdd = new JButton();
        buttonAdd.setText("Add");
        buttonAdd.setMnemonic('A');
        buttonAdd.setDisplayedMnemonicIndex(0);
        panel1.add(buttonAdd, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit = new JButton();
        buttonEdit.setText("Edit");
        buttonEdit.setMnemonic('E');
        buttonEdit.setDisplayedMnemonicIndex(0);
        panel1.add(buttonEdit, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemove = new JButton();
        buttonRemove.setText("Remove");
        buttonRemove.setMnemonic('R');
        buttonRemove.setDisplayedMnemonicIndex(0);
        panel1.add(buttonRemove, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonClose = new JButton();
        buttonClose.setText("Close");
        buttonClose.setMnemonic('C');
        buttonClose.setDisplayedMnemonicIndex(0);
        panel1.add(buttonClose, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        rootPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mainTable = new JTable();
        scrollPane1.setViewportView(mainTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}