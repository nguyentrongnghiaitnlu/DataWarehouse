package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.JDBCConnectionDB;

public class ConnectDatabase {
	JDBCConnectionDB jdbcConnectionDB;
	PreparedStatement preparedStatement;
	Connection connection = null;
	ResultSet resultSet;
	String sql = null;
	PreparedStatement pst = null;

	static final String NUMBER_REGEX = "^[0-9]+$";

	public ConnectDatabase() {
		jdbcConnectionDB = new JDBCConnectionDB();
	}

	public String readFileFromFolder(String file) {
		String result = "";
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), "UTF-8"));
			String line;

			while ((line = bf.readLine()) != null) {
				result += line + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(result);
		return result;
	}

	public Connection connectDBControl() throws SQLException {
		String result;

		result = readFileFromFolder("Configuration/DataConfig.txt");
		String[] connectInfo = result.split("\n");
		String driver = connectInfo[0];
		String url = connectInfo[1];
		String db = connectInfo[2];
		String user = connectInfo[3];
		String password = connectInfo[4];
		connection = jdbcConnectionDB.getJDBConnection(driver, url, db, user, password);
		if (connection != null) {
			System.out.println("Kết nối thành công");
		} else {
			System.out.println("Kết nối thất bại");
		}
		return connection;

	}

	
	public ArrayList<ConnectInfo> getConnectionDataConfig() throws SQLException {
		ArrayList<ConnectInfo> listConnect = new ArrayList<ConnectInfo>();
		connection = connectDBControl();
		String sql = "SELECT * FROM controldb.connection";
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String driver = resultSet.getString(2);
				String url = resultSet.getString(3);
				String db = resultSet.getString(4);
				String user = resultSet.getString(5);
				String password = resultSet.getString(6);
				ConnectInfo connectInfo = new ConnectInfo(driver, url, db, user, password);
				listConnect.add(connectInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listConnect;
	}

	public Connection connectDBStaging() throws SQLException {
		ConnectInfo connectInfo = getConnectionDataConfig().get(1);
		connection = jdbcConnectionDB.getJDBConnection(connectInfo.getDriver(), connectInfo.getUrl(),
				connectInfo.getDb(), connectInfo.getUser(), connectInfo.getPassword());
		System.out.println(connectInfo.getDb());
		return connection;
	}

	public Connection connectDBWarehouse() throws SQLException {
		ConnectInfo connectInfo = getConnectionDataConfig().get(0);
		connection = jdbcConnectionDB.getJDBConnection(connectInfo.getDriver(), connectInfo.getUrl(),
				connectInfo.getDb(), connectInfo.getUser(), connectInfo.getPassword());
		System.out.println(connectInfo.getDb());
		return connection;
	}

	public static void main(String[] args) throws SQLException {
		ConnectDatabase cdc = new ConnectDatabase();
//		cdc.connectDBControl();
//		cdc.connectDBStaging();

	}
}
