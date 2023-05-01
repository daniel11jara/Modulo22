package servelets;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.DAOUsuarioRepository;
import model.ModelLogin;


@WebServlet("/ServeletUsusarioController")
public class ServeletUsusarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
    public ServeletUsusarioController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			
			ModelLogin modelLogin = new ModelLogin();
			
			//id é diferente de nulo e diferente de vazio? converte para um numero se não converte para nulo
			modelLogin.setId(id != null && !id.isEmpty()? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			
			
			daoUsuarioRepository.gravarUsuario(modelLogin);
			
			request.setAttribute("msg", "Operacao Realizada com Sucesso");
			request.setAttribute("modelLogin", modelLogin);//mantem os dados na tela depois de salvo
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();//redericionando para a pagina de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
		
		
		
		
	}

}
