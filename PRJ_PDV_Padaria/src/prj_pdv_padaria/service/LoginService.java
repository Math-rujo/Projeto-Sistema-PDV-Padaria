package prj_pdv_padaria.service;

import prj_pdv_padaria.dao.FuncionarioDAO;
import prj_pdv_padaria.model.Funcionario;

public class LoginService {

    private FuncionarioDAO funcionarioDAO;
    private SenhaService senhaService;

    public LoginService() {
        funcionarioDAO = new FuncionarioDAO();
        
        senhaService = new SenhaService();
    }

    public Funcionario autenticar(String usuario, String senha) {

        Funcionario funcionario = funcionarioDAO.buscarPorUsuario(usuario);

        if (funcionario == null) {
            return null;
        }
        
        boolean senhaValida = senhaService.verificarSenha(senha, funcionario.getSenha());
        
        if(!senhaValida){
            return null;
        }
        return funcionario;
    }
    
}