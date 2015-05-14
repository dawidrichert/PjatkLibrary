package main.java.com.dawidrichert;

import main.java.com.dawidrichert.forms.MainForm;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) throws InstantiationException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException e) {
            System.out.println("Failed to set System Look and Feel Theme.");
        }

        EventQueue.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });
    }
}