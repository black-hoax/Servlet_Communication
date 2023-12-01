
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

@WebServlet(urlPatterns="/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection conn;
    private PreparedStatement ps;
	@Override
	public void init(ServletConfig config) {
		ServletContext context = config.getServletContext();
		String driverClass = config.getInitParameter("org.mariadb.jdbc.Driver");
		//String driverClass = context.getInitParameter("org.mariadb.jdbc.Driver");
		System.out.println(driverClass);
		
		String db = "jdbc:mariadb://mariadb.vamk.fi/e2102970_servlet";
		
		String dbUser = context.getInitParameter("db_User");
		String dbPassword = context.getInitParameter("db_Password");
		System.out.println(dbUser);
		System.out.println(dbPassword);
		
		try { 
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(db, dbUser, dbPassword);
			ps = conn.prepareStatement("SELECT * FROM Login WHERE Username=? AND Password=?");
			System.out.println(conn);
		} catch(SQLException | ClassNotFoundException  e)  {
			e.printStackTrace();
		}
		
		  
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		try {
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			RequestDispatcher reqDis;
			if(rs.next()) { 
				reqDis = req.getRequestDispatcher("homeServlet");
				req.setAttribute("message", "Welcome home: " + email);
				reqDis.forward(req, res);
			} else {
				reqDis = req.getRequestDispatcher("login.html");
				reqDis.include(req, res);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void destroy() {
		try {
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}