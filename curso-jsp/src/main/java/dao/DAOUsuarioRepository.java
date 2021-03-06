package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jdt.internal.compiler.flow.ExceptionHandlingFlowContext;

import com.sun.net.httpserver.Authenticator.Result;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {

	private Connection connection;

	public DAOUsuarioRepository() {

		connection = SingleConnectionBanco.getConnection();

	}

	public ModelLogin gravarUsuario(ModelLogin objeto) throws SQLException {

		if(objeto.isNovo()) {
		
		String sql = "INSERT INTO model_login(login, senha, nome, email) VALUES (?, ? , ? , ?);";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, objeto.getLogin());
		statement.setString(2, objeto.getSenha());
		statement.setString(3, objeto.getNome());
		statement.setString(4, objeto.getEmail());

		statement.execute();
		connection.commit();

		
		}else {
			String sql = "UPDATE public.model_login SET login=?, senha=?, id=?, nome=?, email=? WHERE "+objeto.getId()+";";
			
			PreparedStatement preparesql = connection.prepareStatement(sql);
			
			preparesql.setString(1, objeto.getLogin());
			preparesql.setString(2, objeto.getSenha());
			preparesql.setString(3, objeto.getNome());
			preparesql.setString(4, objeto.getEmail());
			
			preparesql.executeUpdate();
			connection.commit();
		}
		
		return this.consultaUsuario(objeto.getLogin());

	}

	public ModelLogin consultaUsuario(String login) throws SQLException {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "');";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();// traz do banco as informações

		while (resultado.next()) { // se tem resultado
			// objeto recebe , do objeto resultado, com referencia da coluna id o valor do
			// tipo correspondente
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));

		}

		return modelLogin;

	}

	public boolean validarLogin(String login) throws SQLException {

		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "');";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();// traz do banco as informações

		resultado.next(); // Pra ele entrar nos resultados do sql

		return resultado.getBoolean("existe");
		
	}
	
	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM public.model_login WHERE id = ?;";
		PreparedStatement preparedSql = connection.prepareStatement(sql);
		preparedSql.setLong(1,Long.parseLong(idUser));
		preparedSql.executeUpdate();
		connection.commit();
					
	}

}
