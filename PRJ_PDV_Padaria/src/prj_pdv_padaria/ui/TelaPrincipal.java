package prj_pdv_padaria.ui;

import javax.swing.*;
import java.awt.*;

import prj_pdv_padaria.model.Funcionario;

public class TelaPrincipal extends JFrame {

    private Funcionario funcionario;

    public TelaPrincipal(Funcionario funcionario) {

        this.funcionario = funcionario;

        configurarJanela();
        criarMenuBar();
        criarComponentes();
    }

    private void configurarJanela() {

        setTitle("PDV Padaria");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {

        // Painel superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("PDV PADARIA");
        
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel usuario = new JLabel("Usuário: " + funcionario.getNome() + " | Cargo: " + funcionario.getCargo());
        
        usuario.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelSuperior.add(titulo, BorderLayout.NORTH);
        
        painelSuperior.add(usuario, BorderLayout.SOUTH);
        
        add(painelSuperior, BorderLayout.NORTH);

        // Painel dos botões
        JPanel painelBotoes = new JPanel(new GridLayout(5, 1, 20, 20));

        painelBotoes.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        JButton botaoFuncionarios = new JButton("Funcionários");
        JButton botaoProdutos = new JButton("Produtos");
        JButton botaoVendas = new JButton("Nova Venda");
        JButton botaoConsultaVenda = new JButton("Consultar Vendas");
        JButton botaoEstoque = new JButton("Consultar Estoque");

        painelBotoes.add(botaoFuncionarios);
        painelBotoes.add(botaoProdutos);
        painelBotoes.add(botaoVendas);
        painelBotoes.add(botaoConsultaVenda);
        painelBotoes.add(botaoEstoque);

        add(painelBotoes, BorderLayout.CENTER);

        // Eventos dos botões
        botaoFuncionarios.addActionListener(e -> {    
            FuncionarioFrame telaFuncionarios = new FuncionarioFrame();
            telaFuncionarios.setVisible(true);
        });

        botaoProdutos.addActionListener(e -> {
            ProdutoFrame telaProdutos = new ProdutoFrame();
            telaProdutos.setVisible(true);
        });

        botaoVendas.addActionListener(e -> {
            VendaFrame telaVenda = new VendaFrame(funcionario);
            telaVenda.setVisible(true);
        });
        
        botaoConsultaVenda.addActionListener(e ->{
            VendaConsultaFrame telaConsultaVenda = new VendaConsultaFrame();
            telaConsultaVenda.setVisible(true);
        });
        
        botaoEstoque.addActionListener(e -> {
            EstoqueConsultaFrame telaEstoque = new EstoqueConsultaFrame();
            telaEstoque.setVisible(true);
        });
    }
    
    private void criarMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        menuArquivo.add(itemSair);
        
        // Menu Cadastro

        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemFuncionario = new JMenuItem("Funcionário");

        JMenuItem itemProduto = new JMenuItem("Produto");

        menuCadastros.add(itemFuncionario);
        menuCadastros.add(itemProduto);
        
        // Menu Ajuda

        JMenu menuAjuda = new JMenu("Ajuda");

        JMenuItem itemSobre = new JMenuItem("Sobre");

        menuAjuda.add(itemSobre);

        // Consultas
        
        JMenu menuConsultas = new JMenu("Consultas");

        JMenuItem itemVendas = new JMenuItem("Verificar Vendas");

        JMenuItem itemEstoque = new JMenuItem("Consultar Estoque");

        menuConsultas.add(itemVendas);
        menuConsultas.add(itemEstoque);

        menuBar.add(menuArquivo);
        menuBar.add(menuCadastros);
        menuBar.add(menuConsultas);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);
        
        // Eventos

        itemFuncionario.addActionListener(e -> {
            FuncionarioFrame telaFuncionarios = new FuncionarioFrame();
            telaFuncionarios.setVisible(true);
        });

        itemProduto.addActionListener(e -> {
            ProdutoFrame telaProdutos = new ProdutoFrame();
            telaProdutos.setVisible(true);
        });

        itemSobre.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                this,
                "PDV Padaria\n"
                + "Sistema de Ponto de Venda\n\n"
                + "Desenvolvido em Java Swing\n"
                + "Banco de dados PostgreSQL",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        itemVendas.addActionListener(e -> {
            VendaConsultaFrame telaVendas = new VendaConsultaFrame();
            telaVendas.setVisible(true);
        });

        itemEstoque.addActionListener(e -> {
            EstoqueConsultaFrame telaEstoque = new EstoqueConsultaFrame();
            telaEstoque.setVisible(true);
        });

        itemSair.addActionListener(e -> {

            int resposta =
                JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do sistema?",
                    "Sair",
                    JOptionPane.YES_NO_OPTION
                );

            if (resposta == JOptionPane.YES_OPTION) {

                dispose();

                LoginFrame telaLogin =
                    new LoginFrame();

                telaLogin.setVisible(true);
            }
        });
    }
}