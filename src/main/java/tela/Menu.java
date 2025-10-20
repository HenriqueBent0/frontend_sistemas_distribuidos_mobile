package tela;

import javax.swing.*;


public class Menu extends JFrame {

    public Menu() {
        setTitle("📦 Sistema de Estoque");
        setSize(1600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ----- Look and Feel Nimbus -----
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}
