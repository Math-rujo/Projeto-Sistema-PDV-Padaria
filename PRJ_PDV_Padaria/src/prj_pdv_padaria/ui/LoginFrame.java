package prj_pdv_padaria.ui;

import javax.swing.*;
import java.awt.*;
import prj_pdv_padaria.model.Funcionario;
import prj_pdv_padaria.service.LoginService;

public class LoginFrame extends JFrame{
    
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    
    private LoginService loginService;
    
    public LoginFrame(){
        loginService = new LoginService();
        
        configurarJanela();
        criarComponentes();
        
    }
    
    private void configurarJanela(){
        setTitle("PDV Padaria - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void criarComponentes() {

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2, 10, 10));

        painel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel labelUsuario = new JLabel("Usuário:");
        JLabel labelSenha = new JLabel("Senha:");

        campoUsuario = new JTextField();
        campoSenha = new JPasswordField();

        botaoEntrar = new JButton("Entrar");

        painel.add(labelUsuario);
        painel.add(campoUsuario);

        painel.add(labelSenha);
        painel.add(campoSenha);

        painel.add(new JLabel());
        painel.add(botaoEntrar);

        add(painel);

        botaoEntrar.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {

        String usuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());
        Funcionario funcionario = loginService.autenticar(usuario, senha);

        if (funcionario != null) {

            TelaPrincipal telaPrincipal = new TelaPrincipal(funcionario);
            telaPrincipal.setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.","Erro de login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
