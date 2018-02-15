import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/consulta")
public class Consulta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Consulta() {super();}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// obtenemos la informacion que manda el usuario a traves del formulario //
		String autor = request.getParameter("autor");
		
		// prepara la salida del servidor servlet //
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		// objetos de conexion //
		Connection con = null;
		PreparedStatement stmt = null;
		
		// cargar el driver de MySQL //
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			// Credenciales para conectarse a la base de datos //
			String url = "jdbc:mysql://localhost/TiendaLibros";
			String usuario = "librero";
			String password = "Ageofempires2";
			
			// Consula SQL //
			con = DriverManager.getConnection(url, usuario, password);
			String sql = "SELECT * FROM libros WHERE autor = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, autor);
			ResultSet resultados = stmt.executeQuery();
			
			// mostrar resultados //
			out.println("<!DOCTYPE html>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<title>Libros de autores</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Libros de autores</h1>");
			out.println("<p> Libros escritos por " + autor + "</p>");
			int x = 1;
			while(resultados.next()){
				
				out.println("<h3>Libro " + x + ":</h3>");
				out.println("<span><b>Título:</b> " + resultados.getString("titulo") + ".</span><br>");
				out.println("<span><b>Unidades:</b> " + resultados.getString("cantidad") + ".</span><br>");
				out.println("<span><b>Precio:</b> " + resultados.getString("precio") + " Euros.</span><br><hr>");
				x++;
			}
			out.println("</body>");
			out.println("</html>");
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("ERROR AL CONECTAR A LA BASE DE DATOS");
			e.printStackTrace();
		} finally {
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("ERROR AL CERRAR LA CONEXIÓN A LA BASE DE DATOS");
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
