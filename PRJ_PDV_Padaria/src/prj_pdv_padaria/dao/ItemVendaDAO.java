package prj_pdv_padaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prj_pdv_padaria.model.ItemVenda;

public class ItemVendaDAO {
    
    public List<ItemVenda> listar() {

        List<ItemVenda> itens = new ArrayList<>();

        String sql = "SELECT * FROM item_venda";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                ItemVenda item = new ItemVenda();

                item.setIdItemVenda(rs.getInt("id_item_venda"));
                item.setIdVenda(rs.getInt("id_venda"));
                item.setIdProduto(rs.getInt("id_produto"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                item.setSubtotal(rs.getBigDecimal("subtotal"));

                itens.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar itens da venda.");
            e.printStackTrace();
        }

        return itens;
    }
    
    public void cadastrar(ItemVenda item, Connection conexao) throws SQLException {

        String sql = "INSERT INTO item_venda (id_venda, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, item.getIdVenda());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getQuantidade());
            stmt.setBigDecimal(4, item.getPrecoUnitario());
            stmt.setBigDecimal(5, item.getSubtotal());

            stmt.executeUpdate();
        }
    }
}
