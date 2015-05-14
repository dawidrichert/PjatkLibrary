package main.java.com.dawidrichert.utils;

import javax.swing.*;

public class MessageBox {

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    public static void showInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
