package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
     private static String banco = "jdbc:postgresql://localhost:5433/curso-jsp?autoReconnect=true";
     private static String user = "postgres";
     private static String senha = "admin";
     private static Connection connection = null;
     
     public static Connection getConnection() {
		return connection;
	}
     
     
     //duas formas de garantir a conexão
     
     static {//1 - ou chamando a classe direto
    	 conectar();
     }
     
     public SingleConnectionBanco() {//2 - ou instanciar um objeto
		conectar();
	}
     
     private static void conectar() {
    	 try {
    		 
    		 if (connection == null) {
				Class.forName("org.postgresql.Driver");//carrega o drive de conexão do banco
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);//para não fazer alterações no banco sem o nosso caomando
			}
			
		} catch (Exception e) {
			e.printStackTrace();//mostra qualquer erro ao conectar
		}
     }
}
