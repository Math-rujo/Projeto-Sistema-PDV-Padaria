package prj_pdv_padaria.model;

import java.math.BigDecimal;

public class ItemVenda {
    private int idItemVenda;
    private int idVenda;
    private int idProduto;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    
    public int getIdItemVenda(){
        return idItemVenda;
    }
    
    public void setIdItemVenda(int idItemVenda){
        this.idItemVenda = idItemVenda;
    }
    
    public int getIdVenda(){
        return idVenda;
    }
    
    public void setIdVenda(int idVenda){
        this.idVenda = idVenda;
    }
    
    public int getIdProduto(){
        return idProduto;
    }
    
    public void setIdProduto(int idProduto){
        this.idProduto = idProduto;
    }
    
    public int getQuantidade(){
        return quantidade;
    }
    
    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    
    public BigDecimal getPrecoUnitario(){
        return precoUnitario;
    }
    
    public void setPrecoUnitario(BigDecimal precoUnitario){
        this.precoUnitario = precoUnitario;
    }
    
    public BigDecimal getSubtotal(){
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal){
        this.subtotal = subtotal;
    }
}