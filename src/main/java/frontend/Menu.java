package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;
import model.Movimentacao;
import model.Produto;
import remote.EstoqueServico;

public class Menu extends JFrame {

    private EstoqueServico api;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public Menu() {
        setTitle("📦 Sistema de Estoque");
        setSize(1800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ----- Look and Feel Nimbus -----
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // ----- Conexão com RMI -----
        try {
            api = (EstoqueServico) Naming.lookup("rmi://localhost:1099/EstoqueService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar no servidor: " + e.getMessage());
            System.exit(1);
        }

        // ----- Painel de Botões -----
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(240, 248, 255)); // azul bem claro

        JButton btnAdicionar = criarBotao(" Adicionar Produto", new Color(76, 175, 80));
        JButton btnMovimentar = criarBotao(" Registrar Movimentação", new Color(33, 150, 243));
        JButton btnEditar = criarBotao("️ Editar Produto", new Color(255, 193, 7));
        JButton btnExcluir = criarBotao("️ Excluir Produto", new Color(244, 67, 54));
        JButton btnAtualizar = criarBotao(" Atualizar Lista", new Color(100, 181, 246));
        JButton btnListaPrecos = criarBotao(" Lista de Preços", new Color(156, 39, 176));
        JButton btnBalanco = criarBotao(" Balanço", new Color(0, 121, 107));
        JButton btnProdutosCriticos = criarBotao("⚠ Produtos Críticos", new Color(255, 87, 34));
        JButton btnMaisMovimentados = criarBotao("📊 Produtos Movimentados", new Color(63, 81, 181));
        
        painelBotoes.add(btnMaisMovimentados);
        painelBotoes.add(btnProdutosCriticos);
        painelBotoes.add(btnBalanco);
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnMovimentar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnListaPrecos);

        // ----- Tabela -----
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

        // Estilo do cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(60, 141, 188));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // ----- Ações dos Botões -----
        btnAdicionar.addActionListener(e -> abrirJanelaAdicionar());
        btnMovimentar.addActionListener(e -> abrirJanelaMovimentar());
        btnAtualizar.addActionListener(e -> carregarProdutos());
        btnListaPrecos.addActionListener(e -> new TelaListaPrecos().setVisible(true));
        btnBalanco.addActionListener(e -> new TelaBalanco().setVisible(true));
        btnExcluir.addActionListener(e -> excluirProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnProdutosCriticos.addActionListener(e -> new TelaProdutosCriticos().setVisible(true));
        // Duplo clique = editar
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) btnEditar.doClick();
            }
        });

        carregarProdutos();
    }

    // ---------- Métodos auxiliares ----------

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
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

    private void editarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nomeAtual = (String) modeloTabela.getValueAt(linha, 1);
        double precoAtual = (double) modeloTabela.getValueAt(linha, 2);
        String unidadeAtual = (String) modeloTabela.getValueAt(linha, 3);
        int qtdAtual = (int) modeloTabela.getValueAt(linha, 4);
        int minAtual = (int) modeloTabela.getValueAt(linha, 5);
        int maxAtual = (int) modeloTabela.getValueAt(linha, 6);
        String categoriaAtual = (String) modeloTabela.getValueAt(linha, 7);

        JTextField nome = new JTextField(nomeAtual);
        JTextField preco = new JTextField(String.valueOf(precoAtual));
        JTextField unidade = new JTextField(unidadeAtual);
        JTextField qtd = new JTextField(String.valueOf(qtdAtual));
        JTextField qtdMin = new JTextField(String.valueOf(minAtual));
        JTextField qtdMax = new JTextField(String.valueOf(maxAtual));
        JTextField categoria = new JTextField(categoriaAtual);

        Object[] campos = {
                "Nome:", nome,
                "Preço:", preco,
                "Unidade:", unidade,
                "Quantidade:", qtd,
                "Quantidade mínima:", qtdMin,
                "Quantidade máxima:", qtdMax,
                "Categoria:", categoria
        };

        int res = JOptionPane.showConfirmDialog(this, campos, "Editar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                Produto p = new Produto(
                        id,
                        nome.getText(),
                        Double.parseDouble(preco.getText()),
                        unidade.getText(),
                        Integer.parseInt(qtd.getText()),
                        Integer.parseInt(qtdMin.getText()),
                        Integer.parseInt(qtdMax.getText()),
                        categoria.getText()
                );
                api.editarProduto(p);
                carregarProdutos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Verifique os campos numéricos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void abrirJanelaAdicionar() {
        JTextField nome = new JTextField();
        JTextField preco = new JTextField();
        JTextField unidade = new JTextField();
        JTextField qtd = new JTextField();
        JTextField qtdMin = new JTextField();
        JTextField qtdMax = new JTextField();
        JTextField categoria = new JTextField();

        Object[] campos = {
                "Nome:", nome,
                "Preço:", preco,
                "Unidade:", unidade,
                "Quantidade:", qtd,
                "Quantidade mínima:", qtdMin,
                "Quantidade máxima:", qtdMax,
                "Categoria:", categoria
        };

        int res = JOptionPane.showConfirmDialog(this, campos, "Novo Produto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                Produto p = new Produto(
                        0,
                        nome.getText(),
                        Double.parseDouble(preco.getText()),
                        unidade.getText(),
                        Integer.parseInt(qtd.getText()),
                        Integer.parseInt(qtdMin.getText()),
                        Integer.parseInt(qtdMax.getText()),
                        categoria.getText()
                );
                api.adicionarProduto(p);
                carregarProdutos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Verifique os campos numéricos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void abrirJanelaMovimentar() {
        String idStr = JOptionPane.showInputDialog(this, "ID do Produto:");
        if (idStr == null) return;

        String[] opcoes = {"ENTRADA", "SAIDA"};
        String tipo = (String) JOptionPane.showInputDialog(this,
                "Tipo de movimentação:", "Movimentação",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade:");
        if (qtdStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            int qtd = Integer.parseInt(qtdStr);
            Movimentacao m = new Movimentacao(
                    id,
                    java.time.LocalDate.now(),
                    qtd,
                    Movimentacao.Tipo.valueOf(tipo)
            );
            String msg = api.registrarMovimentacao(m);
            JOptionPane.showMessageDialog(this, msg);
            carregarProdutos();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Quantidade/ID inválidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}