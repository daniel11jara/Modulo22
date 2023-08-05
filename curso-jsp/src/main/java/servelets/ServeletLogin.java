package servelets;

import java.io.IOException;

import dao.DAOUsuarioRepository;
import dao.DAOloginRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;

//o chamado controller são as servlets ou ServletLoginController
@WebServlet(urlPatterns = {"/principal/ServeletLogin", "/ServeletLogin"})//mapeamento da url que vem da tela
public class ServeletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DAOloginRepository daoLoginRepository = new DAOloginRepository();
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

    
    public ServeletLogin() {
    }

	//recebe os dados da url em parametros
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String acao = request.getParameter("acao");
		
		if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {//se a acao for diferente de nulo e diferente de vazio
			request.getSession().invalidate();//invalida a sessao
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			redirecionar.forward(request, response);
		}else {
			doPost(request, response);
		}
		
		
	}

	//recebe os dados enviados por um formulario
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String login = request.getParameter("Login");
		String senha = request.getParameter("Senha");
		String url = request.getParameter("url");
		
		try {
		
		//verificado se login e senha estao vazias 
		if(login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			
			//simulando o login
			if (daoLoginRepository.validarAutenticacao(modelLogin)) {
				
				modelLogin = daoUsuarioRepository.consultaUsuario(login);
				
				//mantendo o usuario logado no sistema
				request.getSession().setAttribute("usuario", modelLogin.getLogin());
				request.getSession().setAttribute("isAdmin", modelLogin.getPerfil());
				
				//fazendo a validacao da url
				if (url == null || url.equals("null")) {
					url = "principal/principal.jsp";
				}
				
				//redirecionando para o principal
				RequestDispatcher redirecionar = request.getRequestDispatcher(url);
				redirecionar.forward(request, response);
				
			}else {// se não informou o login e senha retorna pra mesma tela
				RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
				request.setAttribute("msg", "Informe o login e senha corretamente");
				redirecionar.forward(request, response);
			}
			
			
			
		}else {// se não informou o login e senha retorna pra mesma tela
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			request.setAttribute("msg", "Informe o login e senha corretamente");
			redirecionar.forward(request, response);
		}
		
		}catch (Exception e) {
			e.printStackTrace();//redericionando para a pagina de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

}
