package model;

public class Configuration {
//	host_name, port, user_name, password, file_name, file_type, import_dir, success_dir, error_dir, column_list, variable_list, delimeter
	
	private int config_id;
	private String load_library;
	private String host_name;
	private int port;
	private String user_name;
	private String password;
	private String file_name;
	private String file_type;
	private String server_dir;
	private String import_dir;
	private int mode_scp;
	private String success_dir;
	private String error_dir;
	private String column_list;
	private String variable_list;
	private String delimeter;
	
	public Configuration() {
		super();
	}
	public Configuration(int config_id, String host_name, int port, String user_name, String password, String file_name,
			String file_type, String import_dir, String success_dir, String error_dir, String column_list,
			String delimeter) {
		super();
		this.config_id = config_id;
		this.host_name = host_name;
		this.port = port;
		this.user_name = user_name;
		this.password = password;
		this.file_name = file_name;
		this.file_type = file_type;
		this.import_dir = import_dir;
		this.success_dir = success_dir;
		this.error_dir = error_dir;
		this.column_list = column_list;
		this.delimeter = delimeter;
	}
	
	public Configuration(int config_id, String load_library, String host_name, int port, String user_name,
			String password, String file_name, String server_dir, String import_dir, int mode_scp) {
		super();
		this.config_id = config_id;
		this.load_library = load_library;
		this.host_name = host_name;
		this.port = port;
		this.user_name = user_name;
		this.password = password;
		this.file_name = file_name;
		this.server_dir = server_dir;
		this.import_dir = import_dir;
		this.mode_scp = mode_scp;
	}
	public int getConfig_id() {
		return config_id;
	}
	public void setConfig_id(int config_id) {
		this.config_id = config_id;
	}
	public String getHost_name() {
		return host_name;
	}
	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getImport_dir() {
		return import_dir;
	}
	public void setImport_dir(String import_dir) {
		this.import_dir = import_dir;
	}
	public String getSuccess_dir() {
		return success_dir;
	}
	public void setSuccess_dir(String success_dir) {
		this.success_dir = success_dir;
	}
	public String getError_dir() {
		return error_dir;
	}
	public void setError_dir(String error_dir) {
		this.error_dir = error_dir;
	}
	public String getColumn_list() {
		return column_list;
	}
	public void setColumn_list(String column_list) {
		this.column_list = column_list;
	}
	public String getDelimeter() {
		return delimeter;
	}
	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}
	public String getLoad_library() {
		return load_library;
	}
	public void setLoad_library(String load_library) {
		this.load_library = load_library;
	}
	public String getServer_dir() {
		return server_dir;
	}
	public void setServer_dir(String server_dir) {
		this.server_dir = server_dir;
	}
	public int getMode_scp() {
		return mode_scp;
	}
	public void setMode_scp(int mode_scp) {
		this.mode_scp = mode_scp;
	}
	public String getVariable_list() {
		return variable_list;
	}
	public void setVariable_list(String variable_list) {
		this.variable_list = variable_list;
	}
}
