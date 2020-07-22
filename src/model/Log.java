package model;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import constants.Status;

public class Log {
	static String PATH = "Data";

	private int data_file_id;
	private String file_name;
	private String server_name;
	private int data_file_config_id;
	private String file_status;
	private int staging_load_count;
	private String time_stamp_download;
	private long time_stamp_insert_staging;

//	data_file_id, file_name, server_name, data_file_config_id, file_status, staging_load_count, time_stap_download, time_stap_insert_staging

	PreparedStatement pst = null;
	ResultSet rs = null;
	String sql;
	ConnectDatabase cdc;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();
	String timestamp = dtf.format(now);

	private List<Log> listLog;

	public Log() {
		cdc = new ConnectDatabase();
	}

	public Log(String file_name, String server_name, int data_file_config_id, String file_status,
			int staging_load_count, String time_stamp_download, long time_stamp_insert_staging) {
		super();
		this.file_name = file_name;
		this.server_name = server_name;
		this.data_file_config_id = data_file_config_id;
		this.file_status = file_status;
		this.staging_load_count = staging_load_count;
		this.time_stamp_download = time_stamp_download;
		this.time_stamp_insert_staging = time_stamp_insert_staging;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public int getData_file_config_id() {
		return data_file_config_id;
	}

	public void setData_file_config_id(int data_file_config_id) {
		this.data_file_config_id = data_file_config_id;
	}

	public String getFile_status() {
		return file_status;
	}

	public void setFile_status(String file_status) {
		this.file_status = file_status;
	}

	public int getStaging_load_count() {
		return staging_load_count;
	}

	public void setStaging_load_count(int staging_load_count) {
		this.staging_load_count = staging_load_count;
	}

	public String getTime_stamp_download() {
		return time_stamp_download;
	}

	public void setTime_stamp_download(String time_stamp_download) {
		this.time_stamp_download = time_stamp_download;
	}

	public long getTime_stamp_insert_staging() {
		return time_stamp_insert_staging;
	}

	public void setTime_stamp_insert_staging(long time_stamp_insert_staging) {
		this.time_stamp_insert_staging = time_stamp_insert_staging;
	}

	public void insertListLog() {
		List<Log> listLog = readDirectory(PATH);
		for (int i = 0; i < listLog.size(); i++) {
			insertLog(listLog.get(i));
		}
	}

	public List readDirectory(String path) {
		List<Log> listLog = new ArrayList<Log>();
		File dir = new File(path);
		File[] listFile = dir.listFiles();

		for (int i = 0; i < listFile.length; i++) {
			File file = listFile[i];
			Log log = new Log(file.getName(), file.getName().substring(file.getName().lastIndexOf(".")), (i + 1), "ER",
					0, timestamp, 0);
			listLog.add(log);
		}
		return listLog;
	}

	public void insertLog(Log log) {
		sql = "insert into `log` (file_name, server_name, data_file_config_id, file_status, staging_load_count, time_stamp_download, time_stamp_insert_staging) "
				+ "values (?,?,?,?,?,?,?)";
		try {
			pst = cdc.connectDBControl().prepareStatement(sql);
			pst.setString(1, log.getFile_name());
			pst.setString(2, log.getServer_name());
			pst.setInt(3, log.getData_file_config_id());
			pst.setString(4, log.getFile_status());
			pst.setInt(5, log.getStaging_load_count());
			pst.setString(6, log.getTime_stamp_download());
			pst.setLong(7, log.getTime_stamp_insert_staging());
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Log getLog(Status status) {
		sql = "SELECT * FROM controldb.log WHERE file_status = ? limit 1";
		try {
			pst = cdc.connectDBControl().prepareStatement(sql);
			pst.setString(1, status.name());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				this.setData_file_id(rs.getInt("data_file_id"));
				this.setData_file_config_id(rs.getInt("data_file_config_id"));
				this.setTime_stamp_insert_staging(rs.getLong("time_stamp_insert_staging"));
				this.setTime_stamp_download(rs.getString("time_stamp_download"));
				this.setStaging_load_count(rs.getInt("staging_load_count"));
				this.setServer_name(rs.getString("server_name"));
				this.setFile_status(rs.getString("file_status"));
				this.setFile_name(rs.getString("file_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public void updateLog(Status new_status) {
		sql = "UPDATE controldb.log SET file_status = ? WHERE data_file_id = ?";
		try {
			pst = cdc.connectDBControl().prepareStatement(sql);
			pst.setString(1, new_status.name());
			pst.setInt(2, this.getData_file_id());
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setData_file_id(int data_file_id) {
		this.data_file_id = data_file_id;
	}
	public int getData_file_id() {
		return data_file_id;
	}

	public static void main(String[] args) {
		new Log().insertListLog();
	}

}
