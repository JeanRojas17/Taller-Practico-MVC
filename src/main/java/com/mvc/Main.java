package com.mvc;

import com.mvc.views.VistaPrincipalSwing;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new VistaPrincipalSwing().setVisible(true);
        });
    }
}