package main.java.com.dawidrichert;

import main.java.com.dawidrichert.forms.MainForm;
import java.awt.*;

public class Application {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainForm());
    }
}