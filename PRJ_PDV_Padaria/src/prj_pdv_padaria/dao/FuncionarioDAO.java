package prj_pdv_padaria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prj_pdv_padaria.model.Funcionario;

public class FuncionarioDAO {
    
    public List<Funcionario> listar(){
        List<Funcionario> funcionarios = new ArrayList<>();
        
        String sql = "SELECT * FROM funcionario";
        
        try(Connection conexao = Conexao.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()){
            
                while(rs.next()){
                    Funcionario funcionario = new Funcionario();
                    
                    funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setCargo(rs.getString("cargo"));
                    funcionario.setUsuario(rs.getString("usuario"));
                    funcionario.setSenha(rs.getString("senha"));
                    
                    funcionarios.add(funcionario);
                }
        }
        catch(SQLException e){
            System.out.println("Erro ao listar funcionarios.");
            e.printStackTrace();
        }
        
        return funcionarios;
    }
    
    public int cadastrar(Funcionario funcionario) {

        String sql = "INSERT INTO funcionario "
                   + "(nome, cargo, usuario, senha) "
                   + "VALUES (?, ?, ?, ?) "
                   + "RETURNING id_funcionario";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getUsuario());
            stmt.setString(4, funcionario.getSenha());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    int id = rs.getInt("id_funcionario");
                    funcionario.setIdFuncionario(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário.");
            e.printStackTrace();
        }

        return -1;
    }
    
    public void atualizar(Funcionario funcionario) {

        String sql = "UPDATE funcionario SET nome = ?, cargo = ?, usuario = ?, senha = ? WHERE id_funcionario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getUsuario());
            stmt.setString(4, funcionario.getSenha());
            stmt.setInt(5, funcionario.getIdFuncionario());

            stmt.executeUpdate();

            System.out.println("Funcionário atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar funcionário.");
            e.printStackTrace();
        }
    }
        
    public void excluir(int idFuncionario) {

        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            stmt.executeUpdate();

            System.out.println("Funcionário excluído com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir funcionário.");
            e.printStackTrace();
        }
    }
    
    public Funcionario buscarPorUsuario(String usuario) {

        String sql = "SELECT * FROM funcionario WHERE usuario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Funcionario funcionario = new Funcionario();

                    funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setCargo(rs.getString("cargo"));
                    funcionario.setUsuario(rs.getString("usuario"));
                    funcionario.setSenha(rs.getString("senha"));

                    return funcionario;
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar funcionário.");
            e.printStackTrace();
        }

        return null;
    }
    
    public Funcionario buscarPorId(int idFuncionario) {

        String sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Funcionario funcionario = new Funcionario();

                    funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setCargo(rs.getString("cargo"));
                    funcionario.setUsuario(rs.getString("usuario"));
                    funcionario.setSenha(rs.getString("senha"));

                    return funcionario;
                }
            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar funcionário por ID.");
            e.printStackTrace();
        }

        return null;
    }
}
