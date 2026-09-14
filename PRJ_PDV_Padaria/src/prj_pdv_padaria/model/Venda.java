package prj_pdv_padaria.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Venda {
    private int idVenda;
    private LocalDateTime dataVenda;
    private int idFuncionario;
    private BigDecimal total;
    
    public int getIdVenda(){
        return idVenda;
    }
    
    public void setIdVenda(int idVenda){
        this.idVenda = idVenda;
    }
    
    public LocalDateTime getDataVenda(){
        return dataVenda;
    }
    
    public void setDataVenda(LocalDateTime dataVenda){
        this.dataVenda = dataVenda;
    }
    
    public int getIdFuncionario(){
        return idFuncionario;
    }
    
    public void setIdFuncionario(int idFuncionario){
        this.idFuncionario = idFuncionario;
    }
    
    public BigDecimal getTotal(){
        return total;
    }
    
    public void setTotal(BigDecimal total){
        this.total = total;
    }
}
