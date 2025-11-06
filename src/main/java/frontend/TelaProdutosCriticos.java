package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaProdutosCriticos extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaProdutosCriticos() {
        setTitle("⚠ Produtos Abaixo do Mínimo");
        setSize(600, 400);
        setLocationRelativeTo(null);

        modeloTabela = new DefaultTableModel(new String[]{
                "Produto", "Quantidade Mínima", "Quantidade em Estoque"
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
        SwingUtilities.invokeLater(() -> new TelaProdutosCriticos().setVisible(true));
    }
}
