package tela;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Produto;
import remote.EstoqueServico;

public class Menu extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

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

        JButton btnExcluir = criarBotao("️ Excluir Produto", new Color(244, 67, 54));
        painelBotoes.add(btnExcluir);
        add(painelBotoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(new String[]{
                "ID", "Nome", "Preço", "Unidade", "Quantidade", "Mínimo", "Máximo", "Categoria"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(28);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(60, 141, 188));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);

        btnExcluir.addActionListener(e -> excluirProduto());

        carregarProdutos();
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

    private void carregarProdutos() {
        modeloTabela.setRowCount(0);
        try {
            List<Produto> lista = api.listarProdutos();
            for (Produto p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getPrecoUnitario(),
                        p.getUnidade(),
                        p.getQuantidade(),
                        p.getQuantidadeMinima(),
                        p.getQuantidadeMaxima(),
                        p.getCategoria()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void excluirProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(linha, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto?", "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                api.excluirProduto(id);
                carregarProdutos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}
