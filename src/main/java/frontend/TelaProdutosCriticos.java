package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
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

