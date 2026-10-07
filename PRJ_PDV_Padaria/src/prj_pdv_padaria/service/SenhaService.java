package prj_pdv_padaria.service;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaService {
    
    public String gerarHash(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }
    
    public boolean verificarSenha(String senhaDigitada, String hashArmazenado){
        return BCrypt.checkpw(senhaDigitada, hashArmazenado);
    }
}
