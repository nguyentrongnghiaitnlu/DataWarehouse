package model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import constants.Status;

public class Staging {
	private String ordinalNumber;
	private String iD;
	private String lastName;
	private String firstName;
	private String dayBorn;
	private String iDClass;
	private String className;
	private String phoneNumber;
	private String email;
	private String address;
	private String note;

	ConnectDatabase cdb;
	PreparedStatement pst;
	ResultSet rs;

	private static final int NUM = 0;
	private static final int ID = 1;
	private static final int FIRSTNAME = 2;
	private static final int LASTNAME = 3;
	private static final int DOB = 4;
	private static final int IDCLASS = 5;
	private static final int CLASSNAME = 6;
	private static final int NUMBERPHONE = 7;
	private static final int EMAIL = 8;
	private static final int ADDRESS = 9;
	private static final int NOTE = 10;

	public Staging() {
		cdb = new ConnectDatabase();
	}

	public Staging(String ordinalNumber, String iD, String lastName, String firstName, String dayBorn, String iDClass,
			String className, String phoneNumber, String email, String address, String note) {
		super();
		this.ordinalNumber = ordinalNumber;
		this.iD = iD;
		this.lastName = lastName;
		this.firstName = firstName;
		this.dayBorn = dayBorn;
		this.iDClass = iDClass;
		this.className = className;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.note = note;
	}

	public String getOrdinalNumber() {
		return ordinalNumber;
	}

	public void setOrdinalNumber(String ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDayBorn() {
		return dayBorn;
	}

	public void setDayBorn(String dayBorn) {
		this.dayBorn = dayBorn;
	}

	public String getiDClass() {
		return iDClass;
	}

	public void setiDClass(String iDClass) {
		this.iDClass = iDClass;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getColumnList() throws SQLException {
		String result = "";
		Connection connection = cdb.connectDBControl();
		String col = "select column_list, variable_list from controldb.configuration";
		PreparedStatement preparedStatement = connection.prepareStatement(col);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			String[] column_list = rs.getString(1).split(",");
			String[] variable_list = rs.getString(2).split(",");
			for (int i = 0; i < column_list.length; i++) {
				result += column_list[i] + " " + variable_list[i] + " " + null + ",";
			}
			result += "primary key (`" + column_list[0] + "`)";
		}
		return result;
	}

	public void createTable() throws SQLException {
		String result = "";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table if not exists stagingdb.staging(");
		stringBuilder.append(getColumnList());
		stringBuilder.append(");");
//		System.out.println(stringBuilder.toString());
		result = stringBuilder.toString();

		Connection connection = cdb.connectDBStaging();
		PreparedStatement preparedStatement = connection.prepareStatement(result);
		preparedStatement.executeUpdate();
	}

	public List<Student> readData(File file) throws IOException {
		List<Student> students = new ArrayList<Student>();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cells = row.cellIterator();
			Student student = new Student();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				setProps(cell, student);
			}
			if (!standardizedData(student)) {
				continue;
			}
			students.add(student);
		}
		workbook.close();
		inputStream.close();
		return students;
	}

	private static void setProps(Cell cell, Student student) {
		int propsIndex = cell.getColumnIndex();
		Object value = getValue(cell);
		switch (propsIndex) {
		case NUM:
			student.setNum(Integer.parseInt(String.valueOf(value)));
			break;
		case ID:
			student.setId(String.valueOf(value));
			break;
		case FIRSTNAME:
			student.setFirst_name(String.valueOf(value));
			break;
		case LASTNAME:
			student.setLast_name(String.valueOf(value));
			break;
		case DOB:
			student.setDob(String.valueOf(value));
			break;
		case IDCLASS:
			student.setId_class(String.valueOf(value));
			break;
		case CLASSNAME:
			student.setClass_name(String.valueOf(value));
			break;
		case NUMBERPHONE:
			student.setNumber_phone(String.valueOf(value));
			break;
		case EMAIL:
			student.setEmail(String.valueOf(value));
			break;
		case ADDRESS:
			student.setAddress(String.valueOf(value));
			break;
		case NOTE:
			student.setNote(String.valueOf(value));
			break;

		default:
			break;
		}
	}

	private static Object getValue(Cell cell) {
		CellType cellType = cell.getCellTypeEnum();
		Object value = new Object();
		switch (cellType) {
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case NUMERIC:
			value = new BigDecimal(cell.getNumericCellValue()).intValue();
			break;
		case STRING:
			value = cell.getStringCellValue();
			break;
		case FORMULA:
			Workbook workbook = cell.getSheet().getWorkbook();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			value = new BigDecimal(evaluator.evaluate(cell).getNumberValue()).intValue();
			break;
		case BLANK:
		case ERROR:
		case _NONE:
			value = 1;
			break;
		default:
			break;
		}
		return value;
	}

	private boolean standardizedData(Student student) {
		if (student.getNum() == 0 || student.getId() == null) {
			return false;
		}
		return true;
	}

//	Insert into staging
	public void insertStaging(Student student) {
		String sql = "insert into stagingdb.staging(id, first_name, last_name, dob, id_class, class_name, number_phone, email, address, note) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = cdb.connectDBStaging().prepareStatement(sql);
			pst.setString(1, student.getId());
			pst.setString(2, student.getFirst_name());
			pst.setString(3, student.getLast_name());
			pst.setString(4, student.getDob());
			pst.setString(5, student.getId_class());
			pst.setString(6, student.getClass_name());
			pst.setString(7, student.getNumber_phone());
			pst.setString(8, student.getEmail());
			pst.setString(9, student.getAddress());
			pst.setString(10, student.getNote());

			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static boolean loadToStaging(Log log) {
		try {
			Staging staging = new Staging();
			File file = new File("Data" + File.separator + log.getFile_name());
			List<Student> data = staging.readData(file);
			staging.createTable();
			for (Student student : data) {
				System.out.println(student);
				staging.insertStaging(student);
			}
			log.updateLog(Status.TR);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws SQLException, IOException {
		Staging staging = new Staging();
		File dir = new File("G:\\CodeJava\\Data\\sinhvien_chieu_nhom3.xlsx");
		List<Student> data = staging.readData(dir);
		staging.createTable();
		for (Student student : data) {
			System.out.println(student);
			staging.insertStaging(student);
		}
	}
}
