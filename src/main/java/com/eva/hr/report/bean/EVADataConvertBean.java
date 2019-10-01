package com.eva.hr.report.bean;

import com.eva.hr.report.conts.EVAConstants;

public class EVADataConvertBean {
	
	private String empCode = EVAConstants.BLANK;
	private String empName = EVAConstants.BLANK;
	private String department = EVAConstants.BLANK; 
	private String no = EVAConstants.BLANK;
	private String date = EVAConstants.BLANK;
	private String punchKind = EVAConstants.BLANK;
	private String time = EVAConstants.BLANK;
	private String comment = EVAConstants.BLANK;
	private String ot = EVAConstants.BLANK;
	private String late = EVAConstants.BLANK;
	private Integer lateTime = 0;
	private boolean isHoliday = false;
	
	public EVADataConvertBean(){
		
	}
	
	public EVADataConvertBean(String empCode, String empName, String department, String no, String date, String punchKind,
			String time) {
		this.empCode = empCode;
		this.empName = empName;
		this.department = department;
		this.no = no;
		this.date = date;
		this.punchKind = punchKind;
		this.time = time;
	}
	
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPunchKind() {
		return punchKind;
	}
	public void setPunchKind(String punchKind) {
		this.punchKind = punchKind;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLate() {
		return late;
	}

	public void setLate(String late) {
		this.late = late;
	}

	public boolean isHoliday() {
		return isHoliday;
	}

	public void setHoliday(boolean isHoliday) {
		this.isHoliday = isHoliday;
	}
	
	public String getOt() {
		return ot;
	}

	public void setOt(String ot) {
		this.ot = ot;
	}

	public Integer getLateTime() {
		return lateTime;
	}

	public void setLateTime(Integer lateTime) {
		this.lateTime = lateTime;
	}

	@Override
	public String toString() {
		String COMMA = ",";
		StringBuilder stringBuilder = new StringBuilder(empCode)
				.append(COMMA).append(empName).append(COMMA).append(department)
				.append(COMMA).append(no).append(COMMA).append(date).append(COMMA)
				.append(punchKind).append(COMMA).append(time);
		
		return stringBuilder.toString();
	}
	
	public String toStringOut() {
		String COMMA = ",";
		StringBuilder stringBuilder = new StringBuilder(empCode)
				.append(COMMA).append(empName)
				.append(COMMA).append(department)
				.append(COMMA).append(no)
				.append(COMMA).append(date)
				.append(COMMA).append(punchKind)
				.append(COMMA).append(time)
				.append(COMMA).append(comment)
				.append(COMMA).append(late)
				.append(COMMA).append(ot)
				.append(COMMA).append(lateTime != 0 ? lateTime.toString():"");
		
		return stringBuilder.toString();
	}
	
	

}
