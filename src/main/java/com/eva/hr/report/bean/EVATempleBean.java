package com.eva.hr.report.bean;

import com.eva.hr.report.conts.EVAConstants;

public class EVATempleBean {
	
	private String EMP_CODE = EVAConstants.BLANK;
	private String EMP_NAME = EVAConstants.BLANK;
	private String DEPARTMENT = EVAConstants.BLANK;
	private String DAY_COUNT = EVAConstants.BLANK;
	private String DATE = EVAConstants.BLANK;
	private String PUNCH_KIND_1 = EVAConstants.BLANK;
	private String TIME_1 = EVAConstants.BLANK;
	private String PUNCH_KIND_2 = EVAConstants.BLANK;
	private String TIME_2 = EVAConstants.BLANK;
	private String OFF_1 = EVAConstants.BLANK;
	private String OFF_2 = EVAConstants.BLANK;
	private String OFF_ALL = EVAConstants.BLANK;
	private String OT_1 = EVAConstants.BLANK;
	private String OT_2 = EVAConstants.BLANK;
	private String OT_ALL = EVAConstants.BLANK;
	private String REMARK_1 = EVAConstants.BLANK;
	private String REMARK_2 = EVAConstants.BLANK;
	private boolean isSet1 = false;
	private boolean isSet2 = false;
	
	public String getEMP_CODE() {
		return EMP_CODE;
	}

	public void setEMP_CODE(String eMP_CODE) {
		EMP_CODE = eMP_CODE;
	}

	public String getEMP_NAME() {
		return EMP_NAME;
	}

	public void setEMP_NAME(String eMP_NAME) {
		EMP_NAME = eMP_NAME;
	}

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getDAY_COUNT() {
		return DAY_COUNT;
	}

	public void setDAY_COUNT(String dAY_COUNT) {
		DAY_COUNT = dAY_COUNT;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getPUNCH_KIND_1() {
		return PUNCH_KIND_1;
	}

	public void setPUNCH_KIND_1(String pUNCH_KIND_1) {
		PUNCH_KIND_1 = pUNCH_KIND_1;
	}

	public String getTIME_1() {
		return TIME_1;
	}

	public void setTIME_1(String tIME_1) {
		TIME_1 = tIME_1;
	}

	public String getPUNCH_KIND_2() {
		return PUNCH_KIND_2;
	}

	public void setPUNCH_KIND_2(String pUNCH_KIND_2) {
		PUNCH_KIND_2 = pUNCH_KIND_2;
	}

	public String getTIME_2() {
		return TIME_2;
	}

	public void setTIME_2(String tIME_2) {
		TIME_2 = tIME_2;
	}

	public String getOFF_1() {
		return OFF_1;
	}

	public void setOFF_1(String oFF_1) {
		OFF_1 = oFF_1;
	}

	public String getOFF_2() {
		return OFF_2;
	}

	public void setOFF_2(String oFF_2) {
		OFF_2 = oFF_2;
	}

	public String getOFF_ALL() {
		return OFF_ALL;
	}

	public void setOFF_ALL(String oFF_ALL) {
		OFF_ALL = oFF_ALL;
	}

	public String getOT_1() {
		return OT_1;
	}

	public void setOT_1(String oT_1) {
		OT_1 = oT_1;
	}

	public String getOT_2() {
		return OT_2;
	}

	public void setOT_2(String oT_2) {
		OT_2 = oT_2;
	}

	public String getOT_ALL() {
		return OT_ALL;
	}

	public void setOT_ALL(String oT_ALL) {
		OT_ALL = oT_ALL;
	}

	public String getREMARK_1() {
		return REMARK_1;
	}

	public void setREMARK_1(String rEMARK_1) {
		REMARK_1 = rEMARK_1;
	}

	public String getREMARK_2() {
		return REMARK_2;
	}

	public void setREMARK_2(String rEMARK_2) {
		REMARK_2 = rEMARK_2;
	}
	
	public boolean isSet1() {
		return isSet1;
	}

	public void setSet1(boolean isSet1) {
		this.isSet1 = isSet1;
	}

	public boolean isSet2() {
		return isSet2;
	}

	public void setSet2(boolean isSet2) {
		this.isSet2 = isSet2;
	}

	public String toString(){
		return new StringBuilder(EMP_CODE)
				.append(EVAConstants.COMMA).append(EMP_NAME)
				.append(EVAConstants.COMMA).append(DEPARTMENT)
				.append(EVAConstants.COMMA).append(DAY_COUNT)
				.append(EVAConstants.COMMA).append(DATE)
				.append(EVAConstants.COMMA).append(PUNCH_KIND_1)
				.append(EVAConstants.COMMA).append(TIME_1)
				.append(EVAConstants.COMMA).append(OFF_1)
				.append(EVAConstants.COMMA).append(OT_1)
				.append(EVAConstants.COMMA).append(REMARK_1)
				.append(EVAConstants.COMMA).append(PUNCH_KIND_2)
				.append(EVAConstants.COMMA).append(TIME_2)
				.append(EVAConstants.COMMA).append(OFF_2)
				.append(EVAConstants.COMMA).append(OT_2)
				.append(EVAConstants.COMMA).append(REMARK_2)
				.append(EVAConstants.COMMA).append(OFF_ALL)
				.append(EVAConstants.COMMA).append(OT_ALL).toString();
	}
	
	public String getHeader(){
		return EVAConstants.HEADER_CSV2.toString();
	}
	
	

}
