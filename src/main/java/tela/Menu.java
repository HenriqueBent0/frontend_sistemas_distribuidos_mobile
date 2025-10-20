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

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            System.exit(1);
        }

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(240, 248, 255));

        JButton btnAdicionar = criarBotao(" Adicionar Produto", new Color(76, 175, 80));
        JButton btnMovimentar = criarBotao(" Registrar Movimentação", new Color(33, 150, 243));

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnMovimentar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}
