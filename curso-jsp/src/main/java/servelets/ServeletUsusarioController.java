package servelets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.DAOUsuarioRepository;
import model.ModelLogin;


@WebServlet(urlPatterns = {"/ServeletUsusarioController"})
public class ServeletUsusarioController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;
       
    private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
    public ServeletUsusarioController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String acao = request.getParameter("acao");
			
			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				
				String idUser = request.getParameter("id");
				
				daoUsuarioRepository.deletarUser(idUser);
				
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("msg", "Excluido com Sucesso");
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				
			 
					
				
		} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
			
			String nomeBusca = request.getParameter("nomeBusca");
			
			
			List<ModelLogin>  dadosJsonUser = daoUsuarioRepository.consultaUsuarioList(nomeBusca, super.getUserLogado(request));
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dadosJsonUser);
			response.getWriter().write(json);
			
		} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
			String id = request.getParameter("id");
			
			ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioID(id, super.getUserLogado(request));
			
			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
			request.setAttribute("modelLogins", modelLogins);
			
			request.setAttribute("msg", "Usuario em edição");
			request.setAttribute("modelLogin", modelLogin);//mantem os dados na tela depois de salvo 
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		}
			
		else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
			
			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
			
			request.setAttribute("msg", "Usuario carregados");
			request.setAttribute("modelLogins", modelLogins);//mantem os dados na tela depois de salvo 
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		}
			
		else {
			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
			request.setAttribute("modelLogins", modelLogins);
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		}
		
		
		} catch (Exception e) { 
			e.printStackTrace();//redericionando para a pagina de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
		
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String msg = "Operacao Realizada com Sucesso";
			
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String perfil = request.getParameter("perfil");
			
			ModelLogin modelLogin = new ModelLogin();
			
			//id é diferente de nulo e diferente de vazio? converte para um numero se não converte para nulo
			modelLogin.setId(id != null && !id.isEmpty()? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			modelLogin.setPerfil(perfil);
			
			//se ja existe um login repetido e estou tentando gravar um novo registro
			if(daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin == null) {
				msg = "Ja existe usuario com o mesmo login, informe outro login";
			}else {
				
				if (modelLogin.isNovo()) {
					msg = "Gravado com Sucesso";
				}else {
					msg = "Atualizado com sucesso";
				}
				
				modelLogin =  daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogado(request));
			}
			
			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
			request.setAttribute("modelLogins", modelLogins);
			request.setAttribute("msg", msg);
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
