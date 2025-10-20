package tela;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import remote.EstoqueServico;

public class Menu extends JFrame {

    private EstoqueServico api;

    public Menu() {
        setTitle("📦 Sistema de Estoque");
        setSize(1600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ----- Look and Feel Nimbus -----
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // ----- Conexão com RMI -----
        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}
