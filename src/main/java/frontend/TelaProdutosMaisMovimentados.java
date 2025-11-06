package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import remote.EstoqueServico;

public class TelaProdutosMaisMovimentados extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaProdutosMaisMovimentados() {
        setTitle("📊 Produtos Mais Movimentados");
        setSize(600, 300);
        setLocationRelativeTo(null);

        modeloTabela = new DefaultTableModel(new String[]{
                "Tipo", "Produto", "Quantidade Total"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFillsViewportHeight(true);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaProdutosMaisMovimentados().setVisible(true));
    }
}
