package prj_pdv_padaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prj_pdv_padaria.model.Produto;

public class ProdutoDAO {
    
    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setEstoque(rs.getInt("estoque"));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos.");
            e.printStackTrace();
        }

        return produtos;
    }
        
    public void cadastrar(Produto produto) {

        String sql = "INSERT INTO produto (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());

            stmt.executeUpdate();

            System.out.println("Produto cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto.");
            e.printStackTrace();
        }
    }
    
    public void atualizar(Produto produto) {

        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, estoque = ? WHERE id_produto = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setInt(5, produto.getIdProduto());

            stmt.executeUpdate();

            System.out.println("Produto atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto.");
            e.printStackTrace();
        }
    }
    
    public void excluir(int idProduto) {

        String sql = "DELETE FROM produto WHERE id_produto = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);

            stmt.executeUpdate();

            System.out.println("Produto excluído com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto.");
            e.printStackTrace();
        }
    }
    
    public Produto buscarPorId(int idProduto) {

        String sql = "SELECT * FROM produto WHERE id_produto = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Produto produto = new Produto();

                    produto.setIdProduto(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setEstoque(rs.getInt("estoque"));

                    return produto;
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar produto.");
            e.printStackTrace();
        }

        return null;
    }
    
    public void baixarEstoque(int idProduto, int quantidade, Connection conexao) 
            throws SQLException {

        String sql = "UPDATE produto SET estoque = estoque - ? WHERE id_produto = ? AND estoque >= ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, quantidade);
            stmt.setInt(2, idProduto);
            stmt.setInt(3, quantidade);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException(
                    "Estoque insuficiente ou produto não encontrado."
                );
            }
        }
    }
    
    public List<Produto> listarEstoque() {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto ORDER BY nome";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setEstoque(rs.getInt("estoque"));

                produtos.add(produto);
            }

        } catch (SQLException e) {

            System.out.println(
                "Erro ao consultar estoque."
            );

            e.printStackTrace();
        }

        return produtos;
    }
}
