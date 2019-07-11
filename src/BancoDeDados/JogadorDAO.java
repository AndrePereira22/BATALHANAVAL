package BancoDeDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Jogo.Jogador;

public class JogadorDAO {
	
	
	// Método que retorna verdadeiro ou falso para realização do login.
	public boolean login(Connection con, String nome, String senha)
			throws SQLException {

		String sql = "SELECT * FROM jogadores WHERE nome LIKE ? AND senha LIKE ?";

		PreparedStatement preparedStatement = con.prepareStatement(sql);
		preparedStatement.setString(1, nome.toLowerCase().replaceAll("\\s+",""));
		preparedStatement.setString(2, senha.replaceAll("\\s+",""));

		ResultSet rs = preparedStatement.executeQuery();

		if (rs.next()) {
			System.out.println("Loguin Efetuado com sucesso!");
			// Retorna verdadeiro se a consulta for true.
			return true;
		}
		// Retorna falso se a consulta for false.
		return false;
	}

	// Método que valida se já existe registro com nome a ser criado.
	public boolean verificaNome(Connection con, String nome)
			throws SQLException {

		String sql = "SELECT * FROM jogadores WHERE nome LIKE ?";

		PreparedStatement preparedStatement = con.prepareStatement(sql);
		preparedStatement.setString(1, nome.toLowerCase().replaceAll("\\s+",""));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			if (rs.getString("nome").equalsIgnoreCase(nome.toLowerCase().replaceAll("\\s+",""))) {
				// Retorna verdadeiro se a consulta for true.
				return true;
			}
		}
		// Retorna falso se a consulta for false.
		return false;
	}

	// Método que valida senha.
	public boolean verificaSenha(Connection con, String senha, int id)
			throws SQLException {

		String sql = "SELECT senha FROM jogadores WHERE id LIKE ? AND senha=?";
		PreparedStatement preparedStatement = con.prepareStatement(sql);
		preparedStatement.setString(1, String.valueOf(id));
		preparedStatement.setString(2, senha.replaceAll("\\s+",""));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			if (rs.getString("senha").equalsIgnoreCase(senha)) {
				// Retorna verdadeiro se a consulta for true.
				return true;
			}
		}
		// Retorna falso se a consulta for false.
		return false;
	}

	// Adiciona um usuário e senha - Cadastro de Login.
	public boolean addLogin(Connection con, String nome, String senha)
			throws SQLException {

		// Valida nome primeiramente. Cria um novo se o dado não existir no banco.
		if (!verificaNome(con, nome)) {

			String insertSQL = "INSERT INTO jogadores (nome, senha) VALUES (?,?)";

			PreparedStatement preparedStatement = con
					.prepareStatement(insertSQL);
			// Usuário e senha são validados sem espços e o usuário indifere de maiúsculo e minúsculo.
			preparedStatement.setString(1, nome.toLowerCase().replaceAll("\\s+",""));
			preparedStatement.setString(2, senha.replaceAll("\\s+",""));
			preparedStatement.executeUpdate();
			return true;
		}
		
		return false;
	}
	// Método que valida senha.
		public int buscaId(Connection con, String nome)
				throws SQLException {
			int id = 0;
			String sql = "SELECT id FROM jogadores WHERE nome LIKE ?";
			
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, nome.replaceAll("\\s+",""));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
					id = Integer.parseInt(rs.getString("id"));
					return id;
			}
			// Retorna falso se a consulta for false.
			return id;
		}
		
		
		public void save(Connection con, Jogador jogador) throws SQLException {
			if (jogador.getId() == 0) {
				String insertSQL = "INSERT INTO jogadores(imagem, pais, medalhas, vitorias, derrotas, totalJogos) "
						+ "VALUES(?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = con
						.prepareStatement(insertSQL);

				preparedStatement.executeUpdate();
			} else {
				String updateSQL = "UPDATE jogadores SET imagem=?, pais=?, medalhas=?, vitorias=?, derrotas=?, totalJogos=? "
						+ "WHERE id = ?";
				PreparedStatement preparedStatement = con
						.prepareStatement(updateSQL);

				preparedStatement.setInt(7, jogador.getId());
				preparedStatement.executeUpdate();
			}
		}
}