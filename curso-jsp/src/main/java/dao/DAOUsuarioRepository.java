package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
	public DAOUsuarioRepository() {
		// TODO Auto-generated constructor stub
		connection = SingleConnectionBanco.getConnection();
	}
	
	public void gravarUsuario(ModelLogin objeto) throws SQLException  {
		
		String sql = "INSERT INTO model_login(login, senha, nome, email) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement preparedSql = connection.prepareStatement(sql);
		
		preparedSql.setString(1, objeto.getLogin());;
		
	}

}
