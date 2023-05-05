package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
	public DAOUsuarioRepository() {
		// TODO Auto-generated constructor stub
		connection = SingleConnectionBanco.getConnection();
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto) throws Exception  {
		
	
		
		String sql = "INSERT INTO model_login(login, senha, nome, email) VALUES (?, ?, ?, ?);";
		PreparedStatement preparedSql = connection.prepareStatement(sql);
		
		preparedSql.setString(1, objeto.getLogin());
		preparedSql.setString(2, objeto.getSenha());
		preparedSql.setString(3, objeto.getNome());
		preparedSql.setString(4, objeto.getEmail());
		
		preparedSql.execute();//executa a instrucao sql
		connection.commit();//salva os dados
		
		return this.consultaUsuario(objeto.getLogin());
		
	}
	
	public ModelLogin consultaUsuario(String login) throws Exception {
		
		ModelLogin modellogin = new ModelLogin();
		
		String sql = "select * from model_login where upper (login) = upper ('" + login + "')";
		 
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			modellogin.setId(resultado.getLong("id"));
			modellogin.setEmail(resultado.getString("email"));
			modellogin.setLogin(resultado.getString("login"));
			modellogin.setSenha(resultado.getString("senha"));
			modellogin.setNome(resultado.getString("nome"));
		}
		
		return modellogin;
	}

}
