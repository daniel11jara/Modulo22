package servelets;

import java.io.Serializable;
import java.sql.Connection;

import connection.SingleConnectionBanco;
import dao.DAOUsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ServletGenericUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
	
	public Long getUserLogado(HttpServletRequest request) throws Exception {
		
		
		HttpSession session = request.getSession();

		String usuarioLogado = (String) session.getAttribute("usuario");
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();
	}

}
