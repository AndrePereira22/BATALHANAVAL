package BancoDeDados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConexao {

    private static final String BANCO = "batalhanaval";
    private static final String URL = "jdbc:mysql://localhost:3306/" + BANCO;
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    // Criando conexão com a base de dados
    public Connection criarConexao() throws SQLException {

        Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
        return con;

    }

}
