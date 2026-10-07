package prj_pdv_padaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prj_pdv_padaria.model.Telefone;

public class TelefoneDAO {
    
    public List<Telefone> listar() {

        List<Telefone> telefones = new ArrayList<>();

        String sql = "SELECT * FROM telefone";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Telefone telefone = new Telefone();

                telefone.setIdTelefone(rs.getInt("id_telefone"));
                telefone.setIdFuncionario(rs.getInt("id_funcionario"));
                telefone.setTelefone(rs.getString("telefone"));

                telefones.add(telefone);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar telefones.");
            e.printStackTrace();
        }

        return telefones;
    }
    
    public void cadastrar(Telefone telefone) {

        String sql = "INSERT INTO telefone (id_funcionario, telefone) VALUES (?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, telefone.getIdFuncionario());
            stmt.setString(2, telefone.getTelefone());

            stmt.executeUpdate();

            System.out.println("Telefone cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar telefone.");
            e.printStackTrace();
        }
    }
    
    public void atualizar(Telefone telefone) {

        String sql = "UPDATE telefone SET id_funcionario = ?, telefone = ? WHERE id_telefone = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, telefone.getIdFuncionario());
            stmt.setString(2, telefone.getTelefone());
            stmt.setInt(3, telefone.getIdTelefone());

            stmt.executeUpdate();

            System.out.println("Telefone atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar telefone.");
            e.printStackTrace();
        }
    }
    
    public void excluir(int idTelefone) {

        String sql = "DELETE FROM telefone WHERE id_telefone = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idTelefone);

            stmt.executeUpdate();

            System.out.println("Telefone excluído com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir telefone.");
            e.printStackTrace();
        }
    }
    
    public Telefone buscarPorFuncionario(int idFuncionario) {

        String sql =
            "SELECT * FROM telefone "
          + "WHERE id_funcionario = ? "
          + "LIMIT 1";
        
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Telefone telefone = new Telefone();

                    telefone.setIdTelefone(
                        rs.getInt("id_telefone")
                    );

                    telefone.setIdFuncionario(
                        rs.getInt("id_funcionario")
                    );

                    telefone.setTelefone(
                        rs.getString("telefone")
                    );

                    return telefone;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar telefone.");

            e.printStackTrace();
        }

        return null;
    }
    
    public void excluirPorFuncionario(int idFuncionario) {

        String sql =
            "DELETE FROM telefone "
          + "WHERE id_funcionario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            stmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                "Erro ao excluir telefone."
            );

            e.printStackTrace();
        }
    }
}
