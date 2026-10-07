package prj_pdv_padaria.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import prj_pdv_padaria.dao.ProdutoDAO;
import prj_pdv_padaria.model.Produto;

public class EstoqueConsultaFrame extends JFrame {

    private JTable tabelaEstoque;
    private DefaultTableModel modeloTabela;

    private JLabel labelProdutos;
    private JLabel labelItens;

    private ProdutoDAO produtoDAO;

    public EstoqueConsultaFrame() {

        produtoDAO = new ProdutoDAO();

        configurarJanela();
        criarComponentes();
        carregarEstoque();
    }

    private void configurarJanela() {

        setTitle("PDV Padaria - Consultar Estoque");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {

        JPanel painelPrincipal =
            new JPanel(new BorderLayout(10, 10));

        painelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(
                20, 20, 20, 20
            )
        );
        
        // Título

        JLabel titulo = new JLabel("Consultar Estoque");

        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painelPrincipal.add(titulo, BorderLayout.NORTH);
        
        // Resumo

        JPanel painelResumo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        labelProdutos = new JLabel("Produtos: 0");
        labelItens = new JLabel("Itens em estoque: 0");
        labelProdutos.setFont(new Font("Arial", Font.BOLD, 14));
        labelItens.setFont(new Font("Arial", Font.BOLD, 14));

        painelResumo.add(labelProdutos);
        painelResumo.add(labelItens);
        
        // Tabela

        modeloTabela =
            new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Produto",
                    "Descrição",
                    "Preço",
                    "Estoque"
                },
                0
            ) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };

        tabelaEstoque = new JTable(modeloTabela);

        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        
        // Painel central

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));

        painelCentro.add(painelResumo, BorderLayout.NORTH);
        painelCentro.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private void carregarEstoque() {

        modeloTabela.setRowCount(0);

        List<Produto> produtos = produtoDAO.listarEstoque();

        int quantidadeProdutos = produtos.size();

        int quantidadeItens = 0;

        for (Produto produto : produtos) {

            BigDecimal preco = produto.getPreco();

            quantidadeItens += produto.getEstoque();

            modeloTabela.addRow(
                new Object[]{
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getDescricao(),
                    "R$ " + preco,
                    produto.getEstoque()
                }
            );
        }

        labelProdutos.setText("Produtos: " + quantidadeProdutos);
        labelItens.setText("Itens em estoque: " + quantidadeItens);
    }
}