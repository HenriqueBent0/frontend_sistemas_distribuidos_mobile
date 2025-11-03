package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Produto;
import remote.EstoqueServico;

public class TelaProdutosCriticos extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaProdutosCriticos() {
        setTitle("⚠ Produtos Abaixo do Mínimo");
        setSize(600, 400);
        setLocationRelativeTo(null);

        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            dispose();
            return;
        }

        modeloTabela = new DefaultTableModel(new String[]{
                "Produto", "Quantidade Mínima", "Quantidade em Estoque"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFillsViewportHeight(true);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarProdutosCriticos();
    }

    private void carregarProdutosCriticos() {
        modeloTabela.setRowCount(0);

        try {
            List<Produto> lista = api.listarProdutos();
            lista.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome())); // ordem alfabética

            for (Produto p : lista) {
                if (p.getQuantidade() < p.getQuantidadeMinima()) {
                    modeloTabela.addRow(new Object[]{
                            p.getNome(),
                            p.getQuantidadeMinima(),
                            p.getQuantidade()
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos críticos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaProdutosCriticos().setVisible(true));
    }
}