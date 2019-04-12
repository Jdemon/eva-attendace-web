package com.eva.hr.report.process;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eva.hr.report.bean.EVADataConvertBean;
import com.eva.hr.report.bean.EVAProfileBean;
import com.eva.hr.report.bean.EVATempleBean;
import com.eva.hr.report.bot.api.BotFinInstHolidaysData;
import com.eva.hr.report.conts.EVAConstants;
import com.eva.hr.report.service.HolidayService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


@Component
public class ConvertDataProcess {
	
	
	@Value("${temp.path}")
	private String temppath;
	
	@Autowired
	private HolidayService holidayService;

	private static Logger logger = LoggerFactory.getLogger(ConvertDataProcess.class);
	private int Month = 0;
	private String Year = EVAConstants.BLANK;

	public List<String> processStepOne(List<String> fileData) throws Exception {

		List<String> result = new ArrayList<String>();

		boolean isSetDate = false;
		Calendar currentDtm = Calendar.getInstance(Locale.US);
		String department = new String();
		EVAProfileBean evaProfileBean = new EVAProfileBean();
		EVADataConvertBean evaDataConvertBean = new EVADataConvertBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);

		ListIterator<String> litr = fileData.listIterator();
		while (litr.hasNext()) {
			try {
				String[] inputs = litr.next().split("[,]");
				boolean isNamefull = true;

				for (int i = 0; i < inputs.length; i++) {

					if (i == 0) {
						if (StringUtils.isNotBlank(inputs[i].trim())) {
							inputs[i] = inputs[i].trim();
							department = inputs[i];
						}

					} else if (StringUtils.isNotBlank(inputs[i]) && inputs.length == 4) {
						if (!isNamefull && StringUtils.isNotBlank(evaProfileBean.getEmpName())) {
							evaProfileBean.setEmpName(evaProfileBean.getEmpName() + "," + inputs[i]);
							isNamefull = true;
						} else {
							evaProfileBean = new EVAProfileBean();
							evaProfileBean.setDepartment(department);
							evaProfileBean.setEmpName(inputs[i]);
							isNamefull = false;
						}
					} else if (inputs.length == 16) {

						if (StringUtils.isNotBlank(inputs[i])) {
							if (i == 2) {
								evaProfileBean.setEmpCode(inputs[i].trim());
							} else {
								if (i == 5) {
									evaDataConvertBean = new EVADataConvertBean();
									evaDataConvertBean.setNo(inputs[i]);
								} else if (i == 8) {
									evaDataConvertBean.setDate(inputs[i]);
									if (!isSetDate) {
										isSetDate = true;
										sdf.setTimeZone(TimeZone.getDefault());
										Date date = sdf.parse(inputs[i]);
										currentDtm.setTime(date);
										Month = currentDtm.get(Calendar.MONTH);
										Year = StringUtils.substring(inputs[i], 0, 4);
									}
								} else if (i == 12) {
									evaDataConvertBean.setPunchKind(inputs[i]);
								} else if (i == 15) {
									if (inputs[i].length() != 5) {
										inputs[i] = "0" + inputs[i];
									}
									evaDataConvertBean.setTime(inputs[i]);
									evaProfileBean.setPunchBean(evaDataConvertBean);

									evaDataConvertBean = evaProfileBean.getPunchBean();

									result.add(evaDataConvertBean.toString());
								}
							}
						}
					}

				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			} finally {
				litr.remove();
			}
		}
		
		fileData.clear();
		fileData = null;
		return result;

	}
	
	public List<EVADataConvertBean> processStepTwo(List<String> linesList) throws Exception {
		
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.set(Calendar.MONTH,Month);
		cal.set(Calendar.YEAR,Integer.valueOf(Year));
		Integer year = cal.get(Calendar.YEAR);
		int dayMax = cal.getActualMaximum(Calendar.DATE);
		List<String> codeList = new ArrayList<String>();
		
		List<EVADataConvertBean> punchBeans = new ArrayList<EVADataConvertBean>();
		String codeTemp = "";
		for(int i=0;i<linesList.size();i++){
			
			String[] inputs = linesList.get(i).split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
			
			if(!inputs[0].equalsIgnoreCase(codeTemp)){
				codeTemp = inputs[0];
				codeList.add(codeTemp);
			}
			
		}
		
		codeTemp = "";
		Integer index=0;
		for(String empCode : codeList){
			for(Integer d=1 ; d<=dayMax ; d++){
				index++;
				for(Integer i=1 ; i<=2 ; i++){
					EVADataConvertBean beangen = new EVADataConvertBean();
					
					if(!empCode.equalsIgnoreCase(codeTemp)){
						codeTemp = empCode;
						index = 1;
					}
					
					
					beangen.setEmpCode(empCode);
					beangen.setNo(index.toString());
					
					String day = d.toString();
					if(d.toString().length() <2){
						day = "0"+d.toString();
					}
					
					Integer monthnum = Month + 1;
					String month = monthnum.toString();
					if(monthnum.toString().length() <2){
						
						month = "0"+monthnum.toString();
					}
					
					Calendar holiDate = Calendar.getInstance(Locale.US);

					holiDate.set(year, Month, d);
				    if (holiDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || holiDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				    	beangen.setHoliday(true);
				    }
					
					
					beangen.setDate(year.toString()+month+day);
					
					if(i==1){
						beangen.setPunchKind(EVAConstants.IN);
					}else{
						beangen.setPunchKind(EVAConstants.OUT);
					}
					
					
					punchBeans.add(beangen);
					
				}
			}
		}
		
		return punchBeans;
		
	}
	
	public List<String> processStepThree(List<EVADataConvertBean> punchBeans,List<String> linesList) throws Exception {
		
		List<String> result = new ArrayList<String>();
		
		for(EVADataConvertBean beangen : punchBeans){
			
			for(int i=0;i<linesList.size();i++){
				
				String[] inputs = linesList.get(i).split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
				
				if(beangen.getEmpCode().equalsIgnoreCase(inputs[0])){
					beangen.setEmpName(inputs[1]);
					beangen.setDepartment(inputs[2]);
					if(beangen.getDate().equalsIgnoreCase(inputs[4])){
						if(inputs[5].trim().contains(beangen.getPunchKind())){
							String kind = beangen.getPunchKind();
							beangen.setPunchKind(inputs[5]);
							
							String[] time = inputs[6].split("[:]");
							if(kind.equalsIgnoreCase("In") && !beangen.isHoliday()){//&& beangen.getPunchKind().equalsIgnoreCase("Punch In")){
								int hour = Integer.valueOf(time[0]);
								int min = Integer.valueOf(time[1]);
								
								if(hour > 8){
									beangen.setLate("LATE");
								}else if(hour ==8 && min > 30){
									beangen.setLate("LATE");
								}
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(kind.equalsIgnoreCase("Out") && !beangen.isHoliday()){//&& beangen.getPunchKind().equalsIgnoreCase("Punch Out")){
								int hour = Integer.valueOf(time[0]);
								int min = Integer.valueOf(time[1]);
								
								beangen.setLate("");
								
								if(hour < 17){
									beangen.setLate("**");
								}else if(hour == 17 && min < 30){
									beangen.setLate("**");
								}
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(kind.equalsIgnoreCase("Punch Out") && !beangen.isHoliday() && beangen.getPunchKind().equalsIgnoreCase("Punch Out")){
								int hour = Integer.valueOf(time[0]);
								int min = Integer.valueOf(time[1]);
								
								beangen.setLate("");
								
								if(hour < 17){
									beangen.setLate("**");
								}else if(hour == 17 && min < 30){
									beangen.setLate("**");
								}
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(beangen.isHoliday() && (beangen.getPunchKind().equalsIgnoreCase("Overtime In") 
									|| beangen.getPunchKind().equalsIgnoreCase("Overtime Out"))){
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(beangen.isHoliday() && beangen.getPunchKind().equalsIgnoreCase("Punch In")){
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(beangen.isHoliday() && beangen.getPunchKind().equalsIgnoreCase("Punch Out")){
								beangen.setTime("\""+inputs[6]+"\"");
							}
						}else{
							
							if(beangen.isHoliday() && inputs[5].equalsIgnoreCase("Overtime In") && 
									beangen.getPunchKind().equalsIgnoreCase("Punch In")){
								beangen.setPunchKind(inputs[5]);
								beangen.setTime("\""+inputs[6]+"\"");
							}else if(!beangen.isHoliday() 
									&& (beangen.getPunchKind().contains("Out") )
									&& inputs[5].contains("Out")){
								
								beangen.setPunchKind(inputs[5]);
								
								if("Punch Out".equalsIgnoreCase(inputs[5])){
									String[] time = inputs[6].split("[:]");
									int hour = Integer.valueOf(time[0]);
									int min = Integer.valueOf(time[1]);
									
									beangen.setLate("");
									
									if(hour < 17){
										beangen.setLate("**");
									}else if(hour == 17 && min < 30){
										beangen.setLate("**");
									}
								}
								beangen.setTime("\""+inputs[6]+"\"");
							}
							
						}
					}
				}
				
			}
			
			if(beangen.getPunchKind().length() <= 3 && !beangen.isHoliday()){
				String kind = beangen.getPunchKind();
				if(kind.equalsIgnoreCase("In")){
					beangen.setPunchKind("Punch In");
				}else if(kind.equalsIgnoreCase("Out")){
					beangen.setPunchKind("Punch Out");
				}else{
					beangen.setPunchKind("");
				}
				beangen.setTime("-");
			}else if(beangen.getPunchKind().length() <= 3 && beangen.isHoliday()){
				beangen.setPunchKind("");
				beangen.setTime("");
				beangen.setComment("OFF");
			}else if(!(beangen.getPunchKind().length() <= 3) && beangen.isHoliday()){
				beangen.setOt("OT");
			}
			
			result.add(beangen.toStringOut());
			
		}
		return result;
	}
	
	public void checkHoli(EVATempleBean beanv2, LinkedList<BotFinInstHolidaysData> dataSet) throws ParseException {

		LocalDate dateCheck = LocalDate.parse(beanv2.getDATE(), DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYYMMDD));

		for (BotFinInstHolidaysData data : dataSet) {

			LocalDate holiday = LocalDate.parse(data.getDate(), DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD));
			if("-".equals(beanv2.getTIME_1()) && "-".equals(beanv2.getTIME_2())){
				if (holiday.isEqual(dateCheck)) {
					beanv2.setOT_ALL("HOLIDAY");
					break;
				}
			}else {
				if (holiday.isEqual(dateCheck)) {
					beanv2.setOT_ALL("OT");
				}
			}

		}

	}
	
	
	public LinkedList<BotFinInstHolidaysData> getHolidays(String dateStr) throws IOException {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		LocalDate localDate = LocalDate.parse(dateStr, dtf);
		
		return holidayService.getHolidaysDataList(localDate.getYear());
	}
	
	
	public String processStepFour(List<String> linesList,String name) throws Exception {
		
		EVATempleBean beanv2 = new EVATempleBean();
		String tempPath = temppath+name+".csv";
		
		FileUtils.write(new File(tempPath), beanv2.getHeader()+EVAConstants.NEW_LINE
				,EVAConstants.UTF_8,false);
		
		
		LinkedList<BotFinInstHolidaysData> dataSet = null;
		
		for(int i=0;i<linesList.size();i++){
			String[] inputs = linesList.get(i).split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
			
			if(inputs != null && StringUtils.isNotBlank(inputs[4]) &&
					dataSet == null) {
				dataSet = getHolidays(inputs[4]);
			}
			
			if(!beanv2.isSet1()){
				beanv2.setEMP_CODE(inputs[0]);
				beanv2.setEMP_NAME(inputs[1]);
				beanv2.setDEPARTMENT(inputs[2]);
				beanv2.setDAY_COUNT(inputs[3]);
				beanv2.setDATE(inputs[4]);
				beanv2.setPUNCH_KIND_1(inputs[5]);
				beanv2.setTIME_1(inputs[6]);
				beanv2.setOFF_1(inputs[7]);
				beanv2.setREMARK_1(inputs[8]);
				beanv2.setOT_1(inputs[9]);
				
				beanv2.setSet1(true);
			}else if(beanv2.isSet1() && !beanv2.isSet2() &&
					beanv2.getEMP_CODE().equalsIgnoreCase(inputs[0])){
			
				beanv2.setPUNCH_KIND_2(inputs[5]);
				beanv2.setTIME_2(inputs[6]);
				beanv2.setOFF_2(inputs[7]);
				beanv2.setREMARK_2(inputs[8]);
				beanv2.setOT_2(inputs[9]);
				
				if(beanv2.getOFF_1().equalsIgnoreCase(beanv2.getOFF_2())){
					beanv2.setOFF_1(EVAConstants.BLANK);
					beanv2.setOFF_2(EVAConstants.BLANK);
					beanv2.setOFF_ALL(inputs[7]);
				}
				
				if(beanv2.getOT_1().equalsIgnoreCase(beanv2.getOT_2())){
					beanv2.setOT_1(EVAConstants.BLANK);
					beanv2.setOT_2(EVAConstants.BLANK);
					beanv2.setOT_ALL(inputs[9]);
				}
				
				if(beanv2.getTIME_1().equalsIgnoreCase("-") && 
						!beanv2.getTIME_2().equalsIgnoreCase("-")){
					if(StringUtils.isBlank(beanv2.getREMARK_1())){
						beanv2.setREMARK_1("NO PUNCH IN");
					}
					
				}else if(!beanv2.getTIME_1().equalsIgnoreCase("-") && 
						beanv2.getTIME_2().equalsIgnoreCase("-")){
					if(StringUtils.isBlank(beanv2.getREMARK_2())){
						beanv2.setREMARK_2("NO PUNCH OUT");
					}
				}
				
				checkHoli(beanv2, dataSet);
				
				FileUtils.write(new File(tempPath), beanv2.toString()+EVAConstants.NEW_LINE
						,EVAConstants.UTF_8,true);
				
				beanv2.setSet1(false);
				beanv2.setSet2(false);
				beanv2 = new EVATempleBean();
			}
		
		
		}
		return tempPath;
		
	}
	
	
	public byte[] generateReport(String jasper,String temp) throws Exception{
		// Preparing parameters
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("MONTH",Month+1);
		parameters.put("YEAR",Year);
		
		InputStream loadder = JRLoader.getLocationInputStream(temp);
		JRCsvDataSource ds = getDataSource(loadder);
		try{
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);
			
			byte[] report = getReportXls(jasperPrint);
			
			return report;
			
		}finally {
			if(ds != null){
				ds.close();
			}
			if(loadder != null){
				loadder.close();
			}
		}
	}
	
	public byte[] getReportXls(JasperPrint jasperPrint) throws Exception {
		ByteArrayOutputStream xlsReport = null;
		try {
			JRXlsxExporter exporter = new JRXlsxExporter();
			xlsReport = new ByteArrayOutputStream();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
			exporter.exportReport();
			return xlsReport.toByteArray();
		} finally {
			if (xlsReport != null) {
				try {
					xlsReport.close();
				} catch (Exception ignored) {
				}
			}
		}
	}
	
	private JRCsvDataSource getDataSource(InputStream loadder) throws JRException {
		JRCsvDataSource ds = new JRCsvDataSource(loadder);
		ds.setRecordDelimiter(EVAConstants.NEW_LINE);
		ds.setUseFirstRowAsHeader(true);
		return ds;
	}
	
	public List<File> listFile(String directory) {
		List<File> listFile = new ArrayList<File>();
		File dir = new File(directory);
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {

				return name.toLowerCase().endsWith(".csv");
			}
		});

		if (files != null && files.length > 0) {
			listFile = Arrays.asList(files);
		}

		return listFile;

	}

}
