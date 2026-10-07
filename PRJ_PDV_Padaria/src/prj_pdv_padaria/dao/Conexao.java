package prj_pdv_padaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/bd_pdv";

    private static final String USUARIO = System.getenv("PDV_DB_USUARIO");

    private static final String SENHA = System.getenv("PDV_DB_SENHA");

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}