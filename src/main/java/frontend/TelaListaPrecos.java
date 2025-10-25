package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Produto;
import remote.EstoqueServico;

public class TelaListaPrecos extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaListaPrecos() {
        setTitle("Lista de Preços");
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
            "Nome", "Preço Unitário", "Unidade", "Categoria"
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

}
