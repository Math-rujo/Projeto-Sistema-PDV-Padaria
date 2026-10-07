package prj_pdv_padaria.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import prj_pdv_padaria.dao.ProdutoDAO;
import prj_pdv_padaria.model.ItemVenda;
import prj_pdv_padaria.model.Produto;
import prj_pdv_padaria.model.Venda;
import prj_pdv_padaria.model.Funcionario;
import prj_pdv_padaria.service.VendaService;

public class VendaFrame extends JFrame{
    
    private JComboBox<Produto> comboProduto;
    private JTextField campoQuantidade;

    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;
    

    private JLabel labelTotal;

    private JButton botaoAdicionar;
    private JButton botaoFinalizar;
    private JButton botaoCancelar;

    private ProdutoDAO produtoDAO;

    private BigDecimal total = BigDecimal.ZERO;
    
    private List<ItemVenda> itensVenda;
    
    private Funcionario funcionario;

    public VendaFrame(Funcionario funcionario) {

        this.funcionario = funcionario;

        produtoDAO = new ProdutoDAO();

        itensVenda = new ArrayList<>();

        configurarJanela();
        criarComponentes();
        carregarProdutos();
    }
    
    private void configurarJanela() {

        setTitle("PDV Padaria - Nova Venda");

        setSize(900, 600);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLocationRelativeTo(null);
    }
    
    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));

        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
            // Produto e Quantidade

        JPanel painelEntrada = new JPanel(new FlowLayout());

        painelEntrada.add(new JLabel("Produto:"));

        comboProduto = new JComboBox<>();

        comboProduto.setPreferredSize(new Dimension(300, 25));

        painelEntrada.add(comboProduto);

        painelEntrada.add(new JLabel("Quantidade:"));

        campoQuantidade = new JTextField(5);

        painelEntrada.add(campoQuantidade);

        botaoAdicionar = new JButton("Adicionar Item");

        painelEntrada.add(botaoAdicionar);

        painelPrincipal.add(painelEntrada,BorderLayout.NORTH);

        
        // Tabela de itens

        modeloTabela =
            new DefaultTableModel(
                new Object[]{
                    "Produto",
                    "Quantidade",
                    "Preço Unitário",
                    "Subtotal"
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

        tabelaItens = new JTable(modeloTabela);

        JScrollPane scrollTabela = new JScrollPane(tabelaItens);

        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);
        
        // Parte inferior

        JPanel painelInferior = new JPanel(new BorderLayout());

        labelTotal = new JLabel("Total: R$ 0,00");

        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));

        painelInferior.add(labelTotal, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        botaoFinalizar = new JButton("Finalizar Venda");

        botaoCancelar = new JButton("Cancelar");

        painelBotoes.add(botaoFinalizar);

        painelBotoes.add(botaoCancelar);

        painelInferior.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        add(painelPrincipal);
        
        // Eventos

        botaoAdicionar.addActionListener(e -> adicionarItem());
        botaoFinalizar.addActionListener(e -> finalizarVenda());
        botaoCancelar.addActionListener(e -> dispose());
    }
    
    private void carregarProdutos() {
        comboProduto.removeAllItems();

        for (Produto produto : produtoDAO.listar()) {
            comboProduto.addItem(produto);
        }
    }
    
    private void adicionarItem() {

        Produto produtoSelecionado = (Produto) comboProduto.getSelectedItem();

        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        int quantidade;

        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            
        } catch (NumberFormatException e) {
            
            JOptionPane.showMessageDialog(this, "Digite uma quantidade válida.");
            return;
        }

        if (quantidade <= 0) {

            JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
            return;
        }

        if (quantidade > produtoSelecionado.getEstoque()) {

            JOptionPane.showMessageDialog(this, "Quantidade solicitada maior que o estoque disponível.");
            return;
        }

        BigDecimal subtotal = produtoSelecionado.getPreco().multiply(BigDecimal.valueOf(quantidade));

        ItemVenda item = new ItemVenda();

        item.setIdProduto(produtoSelecionado.getIdProduto());

        item.setQuantidade(quantidade);

        item.setPrecoUnitario(produtoSelecionado.getPreco());

        item.setSubtotal(subtotal);

        itensVenda.add(item);

        modeloTabela.addRow(
            new Object[]{
                produtoSelecionado.getNome(),
                quantidade,
                produtoSelecionado.getPreco(),
                subtotal
            }
        );

        total = total.add(subtotal);

        labelTotal.setText(
            "Total: R$ " + total
        );

        campoQuantidade.setText("");
    }

    private void finalizarVenda() {

        if (itensVenda.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item à venda.");
            return;
        }

        int resposta =
            JOptionPane.showConfirmDialog(
                this,
                "Deseja finalizar esta venda?",
                "Confirmar venda",
                JOptionPane.YES_NO_OPTION
            );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        int idFuncionario = funcionario.getIdFuncionario();

        Venda venda = new Venda();

        venda.setIdFuncionario(idFuncionario);

        venda.setDataVenda(LocalDateTime.now());

        venda.setTotal(total);

        VendaService vendaService = new VendaService();

        boolean sucesso = vendaService.registrarVenda(venda, itensVenda);

        if (sucesso) {

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível registrar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
