package main.java.com.dawidrichert.forms;

import main.java.com.dawidrichert.database.Database;
import main.java.com.dawidrichert.database.model.Book;

import javax.swing.*;
import java.awt.event.*;

public class BookForm extends JDialog {
    private JButton buttonCancel;
    private JButton buttonSave;
    private JTextField textFieldPublisher;
    private JPanel rootPanel;
    private JTextField textFieldName;
    private JTextField textFieldAuthor;
    private JTextField textFieldPublicationYear;

    private MainForm mainForm;
    private boolean isEditMode = false;

    public BookForm(MainForm mainForm) {
        this.mainForm = mainForm;

        setContentPane(rootPanel);
        setModal(true);
        pack();
        setLocationRelativeTo(null);

        buttonSave.addActionListener(e -> onSave());
        buttonCancel.addActionListener(e -> onCancel());

        getRootPane().setDefaultButton(buttonSave);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        rootPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public BookForm(MainForm mainForm, Book book) {
        this(mainForm);
        isEditMode = true;
        textFieldName.setText(book.getName());
        textFieldAuthor.setText(book.getAuthor());
        textFieldPublisher.setText(book.getPublisher());
        textFieldPublicationYear.setText(book.getPublicationYear());
    }

    private void onSave() {
        Book book = new Book();
        book.setName(textFieldName.getText());
        book.setAuthor(textFieldAuthor.getText());
        book.setPublisher(textFieldPublisher.getText());
        book.setPublicationYear(textFieldPublicationYear.getText());
        Database.getInstance().saveBook(book);

        mainForm.updateDataTable();

        this.dispose();
    }

    private void onCancel() {
        this.dispose();
    }
}
