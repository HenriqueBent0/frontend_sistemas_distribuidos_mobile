
package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Produto;
import remote.EstoqueServico;

public class TelaBalanco extends JFrame{
    
    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalEstoque;
    
    public TelaBalanco() {
        setTitle("📊 Balanço Físico/Financeiro");
        setSize(700,500);
        setLocationRelativeTo(null);
        
        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            dispose();
            return;
        }
        
        modeloTabela = new DefaultTableModel(new String[]{
            "Produto", "Quantidade", "Preço unitário", "Valor Total"
        }, 0) {
            @Override public boolean isCellEditable(int row,int column) { return false; }
        };
        
        tabela = new JTable(modeloTabela);
        tabela.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
        
        //Rodapé com valor total do estoque
        lblTotalEstoque = new JLabel("Valor Total do Estoque: R$ 0,00");
        lblTotalEstoque.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalEstoque.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTotalEstoque, BorderLayout.SOUTH);
        
        carregarBalanco();
    }
    
    private void carregarBalanco() {
        modeloTabela.setRowCount(0);
        double totalEstoque = 0;
        
        try {
            List<Produto> lista = api.listarProdutos();
            lista.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome())); //ordem alfabética
            
            for (Produto p : lista) {
                double valorTotal = p.getPrecoUnitario() * p.getQuantidade();
                totalEstoque += valorTotal;
                
                modeloTabela.addRow(new Object[]{
                    p.getNome(),
                    p.getQuantidade(),
                    String.format("R$ %.2f", p.getPrecoUnitario()),
                    String.format("R$ %.2f", valorTotal)
                });
            }
            
            lblTotalEstoque.setText("Valor Total do Estoque: R$ " + String.format("%.2f", totalEstoque));
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar balanço: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaBalanco().setVisible(true));
    }
}
