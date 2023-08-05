package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
	public DAOUsuarioRepository() {
		// TODO Auto-generated constructor stub
		connection = SingleConnectionBanco.getConnection();
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception  {
		
		if (objeto.isNovo()) {//grava um novo usuario
			
		String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedSql = connection.prepareStatement(sql);
		
		preparedSql.setString(1, objeto.getLogin());
		preparedSql.setString(2, objeto.getSenha());
		preparedSql.setString(3, objeto.getNome());
		preparedSql.setString(4, objeto.getEmail());
		preparedSql.setLong(5, userLogado);
		preparedSql.setString(6, objeto.getPerfil());
		
		preparedSql.execute();//executa a instrucao sql
		connection.commit();//salva os dados
		
		} else {
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=? perfil=? WHERE id = "+objeto.getId()+";";
			
			PreparedStatement prepareSql = connection.prepareStatement(sql);
			
			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			prepareSql.setString(5, objeto.getPerfil());
			
			prepareSql.executeUpdate();
			
			connection.commit();
					
		}
		
		return this.consultaUsuario(objeto.getLogin(), userLogado);
		
	}
	
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id =" + userLogado;
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {//percorrendo as linhas do resultado sql
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception{
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper (nome) like upper(?) and useradmin is false and usuario_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {//percorrendo as linhas do resultado sql
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		
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
			modellogin.setUseradmin(resultado.getBoolean("useradmin"));
			modellogin.setPerfil(resultado.getString("perfil"));
		}
		
		return modellogin;
	}
	
	
	
public ModelLogin consultaUsuario(String login) throws Exception {
		
		ModelLogin modellogin = new ModelLogin();
		
		String sql = "select * from model_login where upper (login) = upper ('" + login + "') and useradmin is false";
		 
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
	
	
	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {
		
		ModelLogin modellogin = new ModelLogin();
		
		String sql = "select * from model_login where upper (login) = upper ('" + login + "') and useradmin is false and usuario_id =" + userLogado;
		 
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
	
	
public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception {
		
		ModelLogin modellogin = new ModelLogin();
		
		String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";
		 
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);
		
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
	
	public boolean validarLogin(String login) throws Exception {
		
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('"+login+"');";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		return resultado.getBoolean("existe");
	
	}
	
	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login WHERE id = ? and useradmin is false;";
		PreparedStatement prepareSql = connection.prepareStatement(sql);
		prepareSql.setLong(1, Long.parseLong(idUser));
		prepareSql.executeUpdate();
		
		connection.commit();
	}
	

}
