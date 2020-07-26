package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.processing.FilerException;

public class GetConnection {
	String driver = null;
	String url = null;
	String user = null;
	String pass = null;
	String databasebName = null;

	public Connection getConnection(String location_dbName) {
		String pathLinkProperties = "D:\\Messy\\Project Seen\\DataWarehouse-master\\config.properties";
		Connection conn = null;
		if (location_dbName.equalsIgnoreCase("controldb")) {
			try {
				InputStream input = new FileInputStream(pathLinkProperties);
				Properties prop = new Properties();
				prop.load(input);
				driver = prop.getProperty("driver_local");
				url = prop.getProperty("url_local");
				databasebName = prop.getProperty("dbName_controldb");
				user = prop.getProperty("user_local");
				pass = prop.getProperty("pass_local");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else if (location_dbName.equalsIgnoreCase("staging")) {
			try {
				InputStream input = new FileInputStream(pathLinkProperties);
				Properties prop = new Properties();
				prop.load(input);
				driver = prop.getProperty("driver_local");
				url = prop.getProperty("url_local");
				databasebName = prop.getProperty("dbName_staging");
				user = prop.getProperty("user_local");
				pass = prop.getProperty("pass_local");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else if (location_dbName.equalsIgnoreCase("datawarehouse")) {
			try {
				InputStream input = new FileInputStream(pathLinkProperties);
				Properties prop = new Properties();
				prop.load(input);
				driver = prop.getProperty("driver_local");
				url = prop.getProperty("url_local");
				databasebName = prop.getProperty("dbName_datawarehouse");
				user = prop.getProperty("user_local");
				pass = prop.getProperty("pass_local");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		try {
			Class.forName(driver);
			String connectionURL = url + databasebName;
			try {
				conn = DriverManager.getConnection(connectionURL, user, pass);
			} catch (SQLException e) {
				System.out.println("Tai khoan hoac mat khau sai");
				System.exit(0);
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Duong dan file config.properties khong tim thay");
//			System.exit(0);
			e.printStackTrace();
		}

		return conn;
	}

	public static void main(String[] args) {
		Connection conn = new GetConnection().getConnection("staging");

		System.out.println(conn);

	}
}
