package prj_pdv_padaria.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import prj_pdv_padaria.dao.FuncionarioDAO;
import prj_pdv_padaria.model.Funcionario;
import prj_pdv_padaria.dao.TelefoneDAO;
import prj_pdv_padaria.model.Telefone;
import prj_pdv_padaria.service.SenhaService;

public class FuncionarioFrame extends JFrame {
    
    private JTextField campoNome;
    private JTextField campoCargo;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JTextField campoTelefone;

    private JButton botaoCadastrar;
    private JButton botaoAlterar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;
    private JButton botaoVoltar;

    private JTable tabelaFuncionarios;
    private DefaultTableModel modeloTabela;
    
    private FuncionarioDAO funcionarioDAO;
    private TelefoneDAO telefoneDAO;
    
    private SenhaService senhaService;

    private int idFuncionarioSelecionado = -1;

    public FuncionarioFrame() {

        funcionarioDAO = new FuncionarioDAO();
        telefoneDAO = new TelefoneDAO();
        senhaService = new SenhaService();

        configurarJanela();
        criarComponentes();
        carregarFuncionarios();
    }
    
    private void configurarJanela() {
        setTitle("PDV Padaria - Funcionários");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Paínel Formulário

        JPanel painelFormulario = new JPanel(
            new GridLayout(5, 2, 10, 10)
        );

        painelFormulario.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Cargo:"));
        campoCargo = new JTextField();
        painelFormulario.add(campoCargo);
        
        painelFormulario.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField();
        painelFormulario.add(campoTelefone);

        painelFormulario.add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        painelFormulario.add(campoUsuario);

        painelFormulario.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painelFormulario.add(campoSenha);

        painelPrincipal.add(
            painelFormulario,
            BorderLayout.NORTH
        );

        // Botões

        JPanel painelBotoes = new JPanel(
            new FlowLayout()
        );

        botaoCadastrar = new JButton("Cadastrar");
        botaoAlterar = new JButton("Alterar");
        botaoExcluir = new JButton("Excluir");
        botaoLimpar = new JButton("Limpar");

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAlterar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);
        
        
        // Tabela

        modeloTabela = new DefaultTableModel(
            new Object[]{
                "ID",
                "Nome",
                "Cargo",
                "Telefone",
                "Usuário"
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

        tabelaFuncionarios = new JTable(modeloTabela);

        JScrollPane scrollTabela = new JScrollPane(tabelaFuncionarios);
        
        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);
        
        JPanel painelInferior = new JPanel(new BorderLayout());

        
        painelInferior.add(
            painelBotoes,
            BorderLayout.NORTH
        );

        botaoVoltar = new JButton("Voltar");

        painelInferior.add(
            botaoVoltar,
            BorderLayout.SOUTH
        );

        painelPrincipal.add(
            painelInferior,
            BorderLayout.SOUTH
        );

        add(painelPrincipal);
        
        // Eventos

        botaoCadastrar.addActionListener(e -> cadastrarFuncionario());
        botaoAlterar.addActionListener(e -> alterarFuncionario());
        botaoExcluir.addActionListener(e -> excluirFuncionario());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoVoltar.addActionListener(e -> dispose());
        tabelaFuncionarios.getSelectionModel().addListSelectionListener(e -> selecionarFuncionario());
    }

    private void carregarFuncionarios() {

        modeloTabela.setRowCount(0);

        List<Funcionario> funcionarios =
            funcionarioDAO.listar();

        for (Funcionario funcionario : funcionarios) {

            Telefone telefone =
            telefoneDAO.buscarPorFuncionario(
                funcionario.getIdFuncionario()
            );

            String numeroTelefone = "";

            if (telefone != null) {
                numeroTelefone = telefone.getTelefone();
            }
            
            modeloTabela.addRow(
                new Object[]{
                    funcionario.getIdFuncionario(),
                    funcionario.getNome(),
                    funcionario.getCargo(),
                    numeroTelefone,
                    funcionario.getUsuario()
                }
            );
        }
    }

    private void cadastrarFuncionario() {

        if (!validarCampos()) {
            return;
        }

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(campoNome.getText());
        funcionario.setCargo(campoCargo.getText());
        funcionario.setUsuario(campoUsuario.getText());
        
        String senha = new String(campoSenha.getPassword());
        String hashSenha = senhaService.gerarHash(senha);

        funcionario.setSenha(hashSenha);
        
        int idFuncionario = funcionarioDAO.cadastrar(funcionario);
        
        if (idFuncionario != -1) {

            Telefone telefone = new Telefone();

            telefone.setIdFuncionario(idFuncionario);

            telefone.setTelefone(
                campoTelefone.getText().trim()
            );

            telefoneDAO.cadastrar(telefone);

            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");

            limparCampos();
            carregarFuncionarios();
        }
    }

    private void alterarFuncionario() {

        if (idFuncionarioSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.");
            return;
        }

        if (!validarCamposAlteracao()) {
            return;
        }

        // Busca o funcionário atual no banco
        Funcionario funcionarioAtual = funcionarioDAO.buscarPorId(idFuncionarioSelecionado);

        if (funcionarioAtual == null) {

            JOptionPane.showMessageDialog(this, "Funcionário não encontrado.");
            return;
        }

        Funcionario funcionario = new Funcionario();

        funcionario.setIdFuncionario(idFuncionarioSelecionado);
        funcionario.setNome(campoNome.getText());
        funcionario.setCargo(campoCargo.getText());
        funcionario.setUsuario(campoUsuario.getText());

        // Verifica se o usuário digitou uma nova senha
        String senhaDigitada = new String(campoSenha.getPassword());

        if (senhaDigitada.isBlank()) {
            // Mantém o hash que já estava no banco
            funcionario.setSenha(funcionarioAtual.getSenha());

        } else {

            // Gera um novo hash para a nova senha
            String hashSenha = senhaService.gerarHash(senhaDigitada);

            funcionario.setSenha(hashSenha);
        }

        funcionarioDAO.atualizar(funcionario);

        Telefone telefone = telefoneDAO.buscarPorFuncionario(idFuncionarioSelecionado);

        if (telefone != null) {
            
            telefone.setTelefone(campoTelefone.getText().trim());
            telefoneDAO.atualizar(telefone);
            
        } else {

            telefone = new Telefone();
            telefone.setIdFuncionario(idFuncionarioSelecionado);

            telefone.setTelefone(campoTelefone.getText().trim());
            telefoneDAO.cadastrar(telefone);
        }

        JOptionPane.showMessageDialog(this, "Funcionário alterado com sucesso!");

        limparCampos();
        carregarFuncionarios();
    }

    private void excluirFuncionario() {

        if (idFuncionarioSelecionado == -1) {

            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.");
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir este funcionário?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        telefoneDAO.excluirPorFuncionario(
            idFuncionarioSelecionado
        );

        funcionarioDAO.excluir(
            idFuncionarioSelecionado
        );

        JOptionPane.showMessageDialog(
            this,
            "Funcionário excluído com sucesso!"
        );

        limparCampos();
        carregarFuncionarios();
}

    private void selecionarFuncionario() {

        int linha = tabelaFuncionarios.getSelectedRow();

        if (linha == -1) {
            return;
        }

        idFuncionarioSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        campoNome.setText(modeloTabela.getValueAt(linha, 1).toString());
        campoCargo.setText(modeloTabela.getValueAt(linha, 2).toString());
        campoTelefone.setText(modeloTabela.getValueAt(linha, 3).toString());
        campoUsuario.setText(modeloTabela.getValueAt(linha, 4).toString());
        campoSenha.setText("");
    }

    private boolean validarCampos() {

        if (campoNome.getText().trim().isEmpty()
                || campoCargo.getText().trim().isEmpty()
                || campoUsuario.getText().trim().isEmpty()
                || campoSenha.getPassword().length == 0) {

            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return false;
        }

        return true;
    }
    
    private boolean validarCamposAlteracao() {

        if (campoNome.getText().trim().isEmpty()
                || campoCargo.getText().trim().isEmpty()
                || campoUsuario.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios.");
            return false;
        }

        return true;
    }

    private void limparCampos() {

        campoNome.setText("");
        campoCargo.setText("");
        campoTelefone.setText("");
        campoUsuario.setText("");
        campoSenha.setText("");

        idFuncionarioSelecionado = -1;

        tabelaFuncionarios.clearSelection();
    }
}