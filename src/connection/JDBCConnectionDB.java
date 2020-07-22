package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ConnectDatabase;

public class JDBCConnectionDB {
	ResultSet resultSet;
	String sql = null;
	PreparedStatement pst = null;

	private static Connection connection = null;

	public static Connection getJDBConnection(String driver, String url, String db, String user, String password)
			throws SQLException {
		if (connection != null) {
			return connection;
		} else {
			try {
				Class.forName(driver);
				connection = DriverManager.getConnection(url + db, user, password);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} 
//				finally {
//				connection.close();
//			}
		}
		return connection;
	}
}
