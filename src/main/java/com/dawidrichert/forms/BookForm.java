package main.java.com.dawidrichert.forms;

import main.java.com.dawidrichert.database.Database;
import main.java.com.dawidrichert.database.model.Book;
import main.java.com.dawidrichert.utils.MessageBox;
import main.java.com.dawidrichert.utils.NumbersOnlyFilter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class BookForm extends JDialog {

    private final static String windowTitleNewBook = "Add new book";
    private final static String windowTitleEditBook_Format = "Edit book: %s";

    private JButton buttonCancel;
    private JButton buttonSave;
    private JTextField textFieldPublisher;
    private JPanel rootPanel;
    private JTextField textFieldName;
    private JTextField textFieldAuthor;
    private JTextField textFieldPublicationYear;

    private MainForm mainForm;
    private Book book = new Book();

    public BookForm(MainForm mainForm) {
        this.mainForm = mainForm;
        setTitle(windowTitleNewBook);
        initUI();
    }

    public BookForm(MainForm mainForm, Book book) {
        this(mainForm);
        setTitle(String.format(windowTitleEditBook_Format, book.getName()));

        this.book = book;
        textFieldName.setText(book.getName());
        textFieldAuthor.setText(book.getAuthor());
        textFieldPublisher.setText(book.getPublisher());
        textFieldPublicationYear.setText(book.getPublicationYear());
    }

    private void initUI() {
        setContentPane(rootPanel);
        setModal(true);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        initButtons();

        getRootPane().setDefaultButton(buttonSave);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        rootPanel.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        PlainDocument doc = (PlainDocument) textFieldPublicationYear.getDocument();
        doc.setDocumentFilter(new NumbersOnlyFilter());
    }

    private void initButtons() {
        buttonSave.addActionListener(e -> onSave());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onSave() {
        String name = textFieldName.getText();
        String author = textFieldAuthor.getText();
        String publisher = textFieldPublisher.getText();
        String publicationYear = textFieldPublicationYear.getText();

        if(name.length() == 0 || author.length() == 0 || publisher.length() == 0 || publicationYear.length() == 0) {
            MessageBox.showInformation("Please fill out all fields.");
        } else {
            book.setName(name);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setPublicationYear(publicationYear);

            Database.getInstance().saveBook(book);
            mainForm.updateDataTable();
            this.dispose();
        }
    }

    private void onCancel() {
        this.dispose();
    }
}