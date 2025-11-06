package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.sql.*;
import remote.EstoqueServico;

public class TelaProdutosMaisMovimentados extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaProdutosMaisMovimentados() {
        setTitle("📊 Produtos Mais Movimentados");
        setSize(600, 300);
        setLocationRelativeTo(null);

        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            dispose();
            return;
        }

        modeloTabela = new DefaultTableModel(new String[]{
                "Tipo", "Produto", "Quantidade Total"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFillsViewportHeight(true);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        criarTabelaMovimentacao();
    }

    private void criarTabelaMovimentacao() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:estoque.db");
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS movimentacao (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idProduto INTEGER," +
                    "data TEXT," +
                    "quantidade INTEGER," +
                    "tipo TEXT)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar tabela movimentacao: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaProdutosMaisMovimentados().setVisible(true));
    }
}
