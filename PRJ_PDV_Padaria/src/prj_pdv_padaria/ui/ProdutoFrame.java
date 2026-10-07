package prj_pdv_padaria.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import prj_pdv_padaria.dao.ProdutoDAO;
import prj_pdv_padaria.model.Produto;

public class ProdutoFrame extends JFrame{
    
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoPreco;
    private JTextField campoEstoque;

    private JButton botaoCadastrar;
    private JButton botaoAlterar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;
    private JButton botaoVoltar;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private ProdutoDAO produtoDAO;

    private int idProdutoSelecionado = -1;
    
    public ProdutoFrame() {

        produtoDAO = new ProdutoDAO();

        configurarJanela();
        criarComponentes();
        carregarProdutos();
    }
    
    private void configurarJanela() {

        setTitle("PDV Padaria - Produtos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));

        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Formulário

        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));

        painelFormulario.add(new JLabel("Nome:"));

        campoNome = new JTextField();

        painelFormulario.add(campoNome);
        painelFormulario.add(new JLabel("Descrição:"));

        campoDescricao = new JTextField();

        painelFormulario.add(campoDescricao);
        painelFormulario.add(new JLabel("Preço:"));

        campoPreco = new JTextField();

        painelFormulario.add(campoPreco);
        painelFormulario.add(new JLabel("Estoque:"));

        campoEstoque = new JTextField();

        painelFormulario.add(campoEstoque);
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);

        // Tabela

        modeloTabela = new DefaultTableModel(
            new Object[]{
                "ID",
                "Nome",
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

        tabelaProdutos = new JTable(modeloTabela);

        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);

        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);

        // Botões

        JPanel painelBotoes = new JPanel(new FlowLayout());

        botaoCadastrar = new JButton("Cadastrar");

        botaoAlterar = new JButton("Alterar");

        botaoExcluir = new JButton("Excluir");

        botaoLimpar = new JButton("Limpar");

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAlterar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);

        // Painel inferior

        JPanel painelInferior = new JPanel(new BorderLayout());

        painelInferior.add(painelBotoes,BorderLayout.NORTH);

        botaoVoltar = new JButton("Voltar");

        painelInferior.add(botaoVoltar,BorderLayout.SOUTH);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Eventos

        botaoCadastrar.addActionListener(e -> cadastrarProduto());
        botaoAlterar.addActionListener(e -> alterarProduto());
        botaoExcluir.addActionListener(e -> excluirProduto());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoVoltar.addActionListener(e -> dispose());
        
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> selecionarProduto());
    }

    // Listar

    private void carregarProdutos() {

        modeloTabela.setRowCount(0);

        List<Produto> produtos =
            produtoDAO.listar();

        for (Produto produto : produtos) {

            modeloTabela.addRow(
                new Object[]{
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getEstoque()
                }
            );
        }
    }

    // Cadastrar

    private void cadastrarProduto() {

        if (!validarCampos()) {
            return;
        }

        try {

            Produto produto = new Produto();

            produto.setNome(campoNome.getText().trim());
            produto.setDescricao(campoDescricao.getText().trim());
            produto.setPreco(new BigDecimal(campoPreco.getText().trim().replace(",", ".")));
            produto.setEstoque(Integer.parseInt(campoEstoque.getText().trim()));

            produtoDAO.cadastrar(produto);

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");

            limparCampos();
            carregarProdutos();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Preço ou estoque inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Alterar

    private void alterarProduto() {

        if (idProdutoSelecionado == -1) {

            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {

            Produto produto = new Produto();

            produto.setIdProduto(idProdutoSelecionado);
            produto.setNome(campoNome.getText().trim());
            produto.setDescricao(campoDescricao.getText().trim());
            produto.setPreco(new BigDecimal(campoPreco.getText().trim().replace(",", ".")));
            produto.setEstoque(Integer.parseInt(campoEstoque.getText().trim()));
            produtoDAO.atualizar(produto);

            JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!");

            limparCampos();
            carregarProdutos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço ou estoque inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Excluir

    private void excluirProduto() {

        if (idProdutoSelecionado == -1) {

            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }

        int resposta =
            JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este produto?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        produtoDAO.excluir(idProdutoSelecionado);

        JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");

        limparCampos();
        carregarProdutos();
    }

    // Selecionar

    private void selecionarProduto() {

        int linha =
            tabelaProdutos.getSelectedRow();

        if (linha == -1) {
            return;
        }

        idProdutoSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        campoNome.setText(modeloTabela.getValueAt(linha, 1).toString());

        Object descricao =modeloTabela.getValueAt(linha, 2);
        
        campoDescricao.setText(descricao == null? "": descricao.toString());
        campoPreco.setText(modeloTabela.getValueAt(linha, 3).toString());
        campoEstoque.setText(modeloTabela.getValueAt(linha, 4).toString());
    }
    
    // Validação

    private boolean validarCampos() {

        if (campoNome.getText().trim().isEmpty()
                || campoPreco.getText().trim().isEmpty()
                || campoEstoque.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Preencha nome, preço e estoque.");

            return false;
        }

        return true;
    }

    // Limpar

    private void limparCampos() {

        campoNome.setText("");
        campoDescricao.setText("");
        campoPreco.setText("");
        campoEstoque.setText("");

        idProdutoSelecionado = -1;

        tabelaProdutos.clearSelection();
    }
}
