package model;

public class ConnectInfo {
	private String driver;
	private String url;
	private String db;
	private String user;
	private String password;
	public ConnectInfo(String driver, String url, String db, String user, String password) {
		this.driver = driver;
		this.url = url;
		this.db = db;
		this.user = user;
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
