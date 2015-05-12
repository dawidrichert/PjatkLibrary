package main.java.com.dawidrichert;

import main.java.com.dawidrichert.database.BookTableModel;
import main.java.com.dawidrichert.database.model.Book;
import main.java.com.dawidrichert.forms.MainForm;
import java.awt.*;
import java.util.*;

public class Application {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainForm());
    }
}