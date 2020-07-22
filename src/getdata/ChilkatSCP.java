package getdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import model.Configuration;
import model.ConnectDatabase;

public class ChilkatSCP {

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
		
		while(resultSet.next()) {
			configuration.setHost_name(resultSet.getString(1));
			configuration.setPort(resultSet.getInt(2));
			configuration.setUser_name(resultSet.getString(3));
			configuration.setPassword(resultSet.getString(4));
			
			list.add(configuration);
		}
		return list;
	}

	public void downloadSCP(Configuration configuration) {
		CkSsh ssh = new CkSsh();
		CkGlobal ck = new CkGlobal();
		ck.UnlockBundle("Hello team14");
		String hostname = configuration.getHost_name();
		int port = configuration.getPort();
		boolean success = ssh.Connect(hostname, port);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}

		ssh.put_IdleTimeoutMs(5000);
		success = ssh.AuthenticatePw(configuration.getUser_name(), configuration.getPassword());
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		CkScp scp = new CkScp();

		success = scp.UseSsh(ssh);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		scp.put_SyncMustMatch("sinhvien*.*");// down tat ca cac file bat dau bang sinhvien
//		String remotePath = "/volume1/ECEP/song.nguyen/DW_2020/data";
		String remotePath = "/ECEP/song.nguyen/DW_2020/data";
		String localPath = "G:\\CodeJava\\Data"; // thu muc muon down file ve
		success = scp.SyncTreeDownload(remotePath, localPath, 2, false);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}

		ssh.Disconnect();
	}
	
	public static void main(String[] args) throws SQLException {
//		ChilkatSCP chilkatSCP = new ChilkatSCP();
//
//		List<Configuration> configs = chilkatSCP.getSrc();
//		for (Configuration configuration : configs) {
//			chilkatSCP.downloadSCP(configuration);
//		}
		CkGlobal ckGlobal = new CkGlobal();
		ckGlobal.UnlockBundle("abc");
		CkSsh ckSsh = new CkSsh();
		System.out.println(ckSsh != null);
		
	}
}
