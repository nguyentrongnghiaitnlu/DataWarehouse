package model;

public class Student {
	private int num;
	private String id;
	private String first_name;
	private String last_name;
	private String dob;
	private String id_class;
	private String class_name;
	private String number_phone;
	private String email;
	private String address;
	private String note;
	
	public Student() {
		super();
	}
	public Student(int num, String id, String first_name, String last_name, String dob, String id_class,
			String class_name, String number_phone, String email, String address, String note) {
		super();
		this.num = num;
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.dob = dob;
		this.id_class = id_class;
		this.class_name = class_name;
		this.number_phone = number_phone;
		this.email = email;
		this.address = address;
		this.note = note;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getId_class() {
		return id_class;
	}
	public void setId_class(String id_class) {
		this.id_class = id_class;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getNumber_phone() {
		return number_phone;
	}
	public void setNumber_phone(String number_phone) {
		this.number_phone = number_phone;
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
	@Override
	public String toString() {
		return "Student [num=" + num + ", id=" + id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", dob=" + dob + ", id_class=" + id_class + ", class_name=" + class_name + ", number_phone="
				+ number_phone + ", email=" + email + ", address=" + address + ", note=" + note + "]";
	}
}
