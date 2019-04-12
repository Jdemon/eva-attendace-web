package com.eva.hr.report.conts;

public class EVAConstants {
	
	public static final String COMMA = ",";
	public static final String NEW_LINE = "\r\n";
	public static final String PUNCH_IN = "Punch In";
	public static final String PUNCH_OUT = "Punch Out";
	public static final String OVERTIME_IN = "Overtime In";
	public static final String OVERTIME_OUT = "Overtime Out";
	public static final String IN = "In";
	public static final String OUT = "Out";
	public static final String BLANK = "";
	public static final String UTF_8 = "UTF-8";
	public static final String FLAG_Y = "Y";
	public static final String FLAG_N = "N";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	
	
	//Fields Main File
	public static final String EMP_CODE = "EMP_CODE";
	public static final String EMP_NAME = "EMP_NAME";
	public static final String DEPARTMENT = "DEPARTMENT";
	public static final String DAY_COUNT = "DAY_COUNT";
	public static final String DATE = "DATE";
	public static final String PUNCH_KIND = "PUNCH_KIND";
	public static final String TIME = "TIME";
	
	public static StringBuilder HEADER_CSV1 = new StringBuilder(EMP_CODE)
			.append(COMMA).append(EMP_NAME).append(COMMA).append(DEPARTMENT)
			.append(COMMA).append(DAY_COUNT).append(COMMA).append(DATE).append(COMMA)
			.append(PUNCH_KIND).append(COMMA).append(TIME);
	
	//Fields Process File CSV
	public static final String PUNCH_KIND_1 = "PUNCH_KIND_1";
	public static final String TIME_1 = "TIME_1";
	public static final String PUNCH_KIND_2 = "PUNCH_KIND_2";
	public static final String TIME_2 = "TIME_2";
	public static final String OFF_1 = "OFF_1";
	public static final String OFF_2 = "OFF_2";
	public static final String OFF_ALL = "OFF_ALL";
	public static final String OT_1 = "OT_1";
	public static final String OT_2 = "OT_2";
	public static final String OT_ALL = "OT_ALL";
	public static final String REMARK_1 = "REMARK_1";
	public static final String REMARK_2 = "REMARK_2";
	
	public static StringBuilder HEADER_CSV2 = new StringBuilder(EMP_CODE)
			.append(COMMA).append(EMP_NAME)
			.append(COMMA).append(DEPARTMENT)
			.append(COMMA).append(DAY_COUNT)
			.append(COMMA).append(DATE)
			.append(COMMA).append(PUNCH_KIND_1)
			.append(COMMA).append(TIME_1)
			.append(COMMA).append(OFF_1)
			.append(COMMA).append(OT_1)
			.append(COMMA).append(REMARK_1)
			.append(COMMA).append(PUNCH_KIND_2)
			.append(COMMA).append(TIME_2)
			.append(COMMA).append(OFF_2)
			.append(COMMA).append(OT_2)
			.append(COMMA).append(REMARK_2)
			.append(COMMA).append(OFF_ALL)
			.append(COMMA).append(OT_ALL);

}
