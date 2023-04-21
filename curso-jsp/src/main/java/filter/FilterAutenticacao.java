package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;

import connection.SingleConnectionBanco;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//intercepta todas as requisicoes do projeto ou mapeamento
@WebFilter(urlPatterns = { "/principal/*" }) // tudo que vier da pasta principal vai ser interceptado pra fazer
												// validacao
public class FilterAutenticacao extends HttpFilter implements Filter {

	private static Connection connection;

	public FilterAutenticacao() {
		super();
	}

	// encerra os processos quando o servidor é parado
	public void destroy() {
	}

	// intercepta as requisições e as respostas do sistema
	// tudo que é feito no sistema passa por ele
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {

			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();

			String usuarioLogado = (String) session.getAttribute("usuario");

			// url que esta sendo acessado
			String urlParaAutenticar = req.getServletPath();

			// validando se esta logado
			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {

				javax.servlet.RequestDispatcher redireciona = request
						.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "por favor realize o login!");
				redireciona.forward(request, response);
				return;// para a execução e redireciona o login
			} else {
				chain.doFilter(request, response);
			}

			// deu tudo certo entao comita tudo no banco de dados
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			
			javax.servlet.RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			
			
			
			try {
				connection.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		}

	}

	// inicia os processos ou recursos quando o servidor sobe os projetos
	// iniciar a conexao com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}

}
