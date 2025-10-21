
package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Produto;
import remote.EstoqueServico;

public class TelaBalanco extends JFrame{
    private EstoqueServicos api;
    private Jtable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalEstoque
    
    public TelaBalanco() {
        setTitle("📊 Balanço Físico/Financeiro");
        setSize(700,500);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaBalanco().setVisible(true));
    }
}
