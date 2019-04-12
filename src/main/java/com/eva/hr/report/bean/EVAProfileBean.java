package com.eva.hr.report.bean;

public class EVAProfileBean {
	
	private String empCode; 
	private String empName;
	private String department;
	private EVADataConvertBean punchBean;
	
	public EVAProfileBean(){}

	public EVAProfileBean(String empCode, String empName, String department) {
		this.setEmpCode(empCode)
		.setEmpName(empName)
		.setDepartment(department);
	}

	public String getEmpCode() {
		return empCode;
	}

	public EVAProfileBean setEmpCode(String empCode) {
		this.empCode = empCode;
		return this;
	}

	public String getEmpName() {
		return empName;
	}

	public EVAProfileBean setEmpName(String empName) {
		this.empName = empName;
		return this;
	}

	public String getDepartment() {
		return department;
	}

	public EVAProfileBean setDepartment(String department) {
		this.department = department;
		return this;
	}

	public EVADataConvertBean getPunchBean() {
		return punchBean;
	}

	public void setPunchBean(EVADataConvertBean punchBean) {
		this.punchBean = new EVADataConvertBean(empCode, empName, department, punchBean.getNo()
				, punchBean.getDate(), punchBean.getPunchKind(),punchBean.getTime());
	}

	@Override
	public String toString() {
		
		return new StringBuilder("EVAProfileBean [empCode=")
				.append(empCode).append(", empName=").append(empName)
				.append(", department=").append(department).append("]").toString();
		
	}

}
