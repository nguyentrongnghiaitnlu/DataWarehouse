package getdata;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import model.Configuration;
import model.ConnectDatabase;
import model.GetConnection;
import model.SendMail;

public class ChilkatSCP {
	SendMail sm = new SendMail();
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String sql;
	private Connection conn = null;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();
	String timestamp = dtf.format(now);
	static final String NUMBER_REGEX = "^[0-9]+$";
	String server_path;
	String local_path;
	private String sqlCheck;
	private PreparedStatement pstCheck = null;
	private ResultSet rsCheck;
	static {
		try {
			System.loadLibrary("chilkat"); // copy file chilkat.dll vao thu muc project
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	ConnectDatabase cdb;

	public ChilkatSCP() {
		cdb = new ConnectDatabase();
	}

	public List<Configuration> getSrc() throws SQLException {
		Configuration configuration = new Configuration();
		List<Configuration> list = new ArrayList<Configuration>();
		String sql = "select host_name, port, user_name, password from controldb.configuration";
		Connection connection = cdb.connectDBControl();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			configuration.setHost_name(resultSet.getString(1));
			configuration.setPort(resultSet.getInt(2));
			configuration.setUser_name(resultSet.getString(3));
			configuration.setPassword(resultSet.getString(4));

			list.add(configuration);
		}
		return list;
	}

	public boolean chilkatSCPDownLoad(Configuration config) {
		CkSsh ssh = new CkSsh();
		CkGlobal ck = new CkGlobal();
		ck.UnlockBundle("Hello Team14");
		// 1.1. Kiểm tra kết nối đến address server thành công hay chưa?
		boolean success = ssh.Connect(config.getHost_name(), config.getPort());
		if (success != true) {

			// 1.1.1. sendMail error hostname or port
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Server không tìm thấy...!!");
			return false;
		}

		ssh.put_IdleTimeoutMs(5000);
		// 1.2. Kiểm tra tài khoản và mặt khẩu kết nối đến address server thành công hay
		// chưa?
		success = ssh.AuthenticatePw(config.getUser_name(), config.getPassword());
		if (success != true) {
			// 1.2.1. sendmail error username or password
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Tài khoản hoặc mặt khẩu sai!...");
			return false;
		}
		CkScp scp = new CkScp();
		// 1.3. Kiểm tra sử dụng SSH với tài khoản và mặt khẩu kết nối đến address
		// server thành công hay chưa?
		success = scp.UseSsh(ssh);
		if (success != true) {
			// 1.3.1. sendMail error using ssh
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Không có kết nối!...");
			return false;
		}
		scp.put_SyncMustMatch(config.getFile_name());
//		scp.put_SyncMustNotMatch(synMustMath);
		// không tải những file đã tải rồi
		success = scp.SyncTreeDownload(config.getServer_dir(), config.getImport_dir(), config.getMode_scp(), false);
		// 1.4. Kiểm tra đã tải được file hay chưa
		if (success != true) {
			// 1.4.1. sendMail error download fail
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE",
					"Thư mực server hoặc địa chỉ local không tìm thấy!...");
			return false;
		}
		// 1.4.2. sendMail download success
		sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Đã tải file thành công!...");
		ssh.Disconnect();
		return true;
	}

	public boolean isDownLoadSCPChilkat(int id) {
		boolean result = false;
		sql = "SELECT * FROM configuration where config_id = '" + id + "'";
		try {
			// 1. select fields table SCP in data SCP using download
			pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				int id_scp = rs.getInt("config_id");
				String load_library = rs.getString("load_library");
				String host_name_scp = rs.getString("host_name_scp");
				int port_scp = rs.getInt("port_scp");
				String username_scp = rs.getString("username_scp");
				String password_scp = rs.getString("password_scp");
				String sync_must_math = rs.getString("file_name");
				server_path = rs.getString("server_dir");
				local_path = rs.getString("import_dir");
				int mode_scp = rs.getInt("mode_scp");
				Configuration scp_config = new Configuration(id_scp, load_library, host_name_scp, port_scp,
						username_scp, password_scp, sync_must_math, server_path, local_path, mode_scp);

				if (chilkatSCPDownLoad(scp_config)) {
					result = true;
					return result;
				} else {
					return result;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return result;

		} finally {
			try {
				if (pst != null)
					pst.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	public boolean insertDataLog(int id) {
		int rs = 0;
		boolean check = false;
		if (!isDownLoadSCPChilkat(id)) {
			return check;
		} else {
			sql = "INSERT INTO log (file_name,data_file_config_id,file_status,staging_load_count, timestamp_download, timestamp_insert_staging, timestamp_insert_datawarehouse)"
					+ " values (?,?,?,?,?,?,?)";
			File localPath = new File(local_path);
			File[] listFileLog = localPath.listFiles();

			lable: for (int i = 0; i < listFileLog.length; i++) {
				String sqlCheck = "select * from log";
				try {
					pstCheck = new GetConnection().getConnection("controldb").prepareStatement(sqlCheck);
					rsCheck = pstCheck.executeQuery();
					while (rsCheck.next()) {
						if (rsCheck.getString(2).equals(listFileLog[i].getName())) {
							continue lable;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return check;
				} finally {
					try {
						if (pstCheck != null)
							pstCheck.close();
						if (this.rsCheck != null)
							this.rsCheck.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

				try {
					pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
					pst.setString(1, listFileLog[i].getName());
//								System.out.println(listFileLog[i].getName());
					pst.setInt(2, id);
					pst.setString(3, "ER");
					pst.setInt(4, 0);
					pst.setString(5, timestamp);
					pst.setString(6, null);
					pst.setString(7, null);
					rs = pst.executeUpdate();
					check = true;
				} catch (Exception e) {
					e.printStackTrace();
					return check;
				} finally {
					try {
						if (pst != null)
							pst.close();
						if (this.rs != null)
							this.rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}
		}

		return check;

	}
	public void sendMailInsertLog(int id) {
		if (insertDataLog(id) == true) {
			// 1.6.1 if success, sendmail with content success
//			sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Ghi log thành công rồi bồ tèo...!");
			System.out.println("Send mail write log success!!!");
		} else {
			// 1.6.2 if fail, sendmail with content fail
//			sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Ghi log fail rồi bồ tèo...!");
			System.out.println("Send mail write log fail!!!");

		}
	}
	public static void main(String[] args) throws SQLException {
		ChilkatSCP test = new ChilkatSCP();
//		Configuration config = new Configuration(1, "chilkat", "drive.ecepvn.org", 2227, "guest_access", "123456", "sinhvien*.xlsx", "/volume1/ECEP/song.nguyen/DW_2020/data", "D:\\HK6 2019 - 2020\\Data WAREHOUSE\\DIR\\Student", 6);
//		test.chilkatSCPDownLoad(config);
//		test.isDownLoadSCPChilkat(1);
		test.insertDataLog(1);
//		test.sendMailInsertLog(1);
	}
}
