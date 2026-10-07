package prj_pdv_padaria.dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prj_pdv_padaria.model.ItemVenda;

import prj_pdv_padaria.model.Venda;

public class VendaDAO {
    
    public List<Venda> listar() {

        List<Venda> vendas = new ArrayList<>();

        String sql = "SELECT * FROM venda";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Venda venda = new Venda();

                venda.setIdVenda(rs.getInt("id_venda"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                venda.setIdFuncionario(rs.getInt("id_funcionario"));
                venda.setTotal(rs.getBigDecimal("total"));

                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas.");
            e.printStackTrace();
        }

        return vendas;
    }
    
    public int cadastrar(Venda venda, Connection conexao) throws SQLException {

        String sql = "INSERT INTO venda (data_venda, id_funcionario, total) VALUES (?, ?, ?) RETURNING id_venda";

         try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

             stmt.setTimestamp(
                 1, java.sql.Timestamp.valueOf(venda.getDataVenda())
             );

             stmt.setInt(2, venda.getIdFuncionario());
             stmt.setBigDecimal(3, venda.getTotal());

             try (ResultSet rs = stmt.executeQuery()) {

                 if (rs.next()) {
                     return rs.getInt("id_venda");
                 }
             }
         }

         throw new SQLException("Não foi possível obter o ID da venda.");
     }
    
    public List<Object[]> listarVendasDetalhadas() {

        List<Object[]> vendas = new ArrayList<>();

        String sql = "SELECT v.id_venda, v.data_venda, f.nome AS funcionario, v.total FROM venda v "
                + "INNER JOIN funcionario f ON v.id_funcionario = f.id_funcionario ORDER BY v.data_venda DESC";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Object[] venda = {
                    rs.getInt("id_venda"),
                    rs.getTimestamp("data_venda"),
                    rs.getString("funcionario"),
                    rs.getBigDecimal("total")
                };

                vendas.add(venda);
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar vendas.");
            e.printStackTrace();
        }

        return vendas;
    }
    
    public List<Object[]> listarVendasPorPeriodo(Timestamp inicio, Timestamp fim) {

        List<Object[]> vendas = new ArrayList<>();

        String sql =
            "SELECT "
          + "v.id_venda, "
          + "v.data_venda, "
          + "f.nome AS funcionario, "
          + "v.total "
          + "FROM venda v "
          + "INNER JOIN funcionario f "
          + "ON v.id_funcionario = f.id_funcionario "
          + "WHERE v.data_venda >= ? "
          + "AND v.data_venda < ? "
          + "ORDER BY v.data_venda DESC";

        try (Connection conexao = Conexao.conectar(); 
                PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setTimestamp(1, inicio);
            stmt.setTimestamp(2, fim);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Object[] venda = {
                        rs.getInt("id_venda"),
                        rs.getTimestamp("data_venda"),
                        rs.getString("funcionario"),
                        rs.getBigDecimal("total")
                    };

                    vendas.add(venda);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                "Erro ao listar vendas por período."
            );

            e.printStackTrace();
        }

        return vendas;
    }
}
