package prj_pdv_padaria.model;

public class Telefone {
    private int idTelefone;
    private int idFuncionario;
    private String telefone;
    
    public int getIdTelefone(){
        return idTelefone;
    }
    
    public void setIdTelefone(int idTelefone){
        this.idTelefone = idTelefone;
    }
    
    public int getIdFuncionario(){
        return idFuncionario;
    }
    
    public void setIdFuncionario(int idFuncionario){
        this.idFuncionario = idFuncionario;
    }
    
    public String getTelefone(){
        return telefone;
    }
    
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
}
