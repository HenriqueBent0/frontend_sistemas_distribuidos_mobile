package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;
import model.Produto;
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

        carregarMaisMovimentados();
    }

    private void carregarMaisMovimentados() {
        modeloTabela.setRowCount(0);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:estoque.db")) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS movimentacao (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idProduto INTEGER," +
                        "data TEXT," +
                        "quantidade INTEGER," +
                        "tipo TEXT)");
            }

            String sqlMaisEntrada = "SELECT idProduto, SUM(quantidade) as total " +
                    "FROM movimentacao WHERE tipo='ENTRADA' GROUP BY idProduto ORDER BY total DESC LIMIT 1";
            String sqlMaisSaida = "SELECT idProduto, SUM(quantidade) as total " +
                    "FROM movimentacao WHERE tipo='SAIDA' GROUP BY idProduto ORDER BY total DESC LIMIT 1";

            List<Produto> produtos = api.listarProdutos();

            try (Statement st = conn.createStatement(); ResultSet rsEntrada = st.executeQuery(sqlMaisEntrada)) {
                if (rsEntrada.next()) {
                    int idProd = rsEntrada.getInt("idProduto");
                    int total = rsEntrada.getInt("total");
                    Produto p = produtos.stream().filter(prod -> prod.getId() == idProd).findFirst().orElse(null);
                    if (p != null) modeloTabela.addRow(new Object[]{"Mais Entradas", p.getNome(), total});
                }
            }

            try (Statement st = conn.createStatement(); ResultSet rsSaida = st.executeQuery(sqlMaisSaida)) {
                if (rsSaida.next()) {
                    int idProd = rsSaida.getInt("idProduto");
                    int total = rsSaida.getInt("total");
                    Produto p = produtos.stream().filter(prod -> prod.getId() == idProd).findFirst().orElse(null);
                    if (p != null) modeloTabela.addRow(new Object[]{"Mais Saídas", p.getNome(), total});
                }
            }

        } catch (SQLException | RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos movimentados: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaProdutosMaisMovimentados().setVisible(true));
    }
}
