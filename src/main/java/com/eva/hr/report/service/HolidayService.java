package com.eva.hr.report.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eva.hr.report.bot.api.BotFinInstHolidaysData;
import com.eva.hr.report.bot.api.BotRootResp;
import com.eva.hr.report.conts.EVAConstants;

@Service
public class HolidayService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${resource.path}")
	private String resource;
	
	
	public String deldate(String date) {
		
		LocalDate deldate = LocalDate.parse(date
				,DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD));
		
		File holiFile = new File(resource+deldate.getYear()+".txt");
		
		List<String> evaholidays = null;
		if(holiFile.exists()) {
			try {
				evaholidays = FileUtils.readLines(holiFile,StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		List<String> newDatas = new ArrayList<>();
		if(evaholidays != null && !evaholidays.isEmpty()) {
			for(String evaholiday : evaholidays) {
				if(!evaholiday.contains(date)) {
					newDatas.add(evaholiday);
				}
			}
		}
		
		try {
			FileUtils.writeLines(holiFile, newDatas);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return String.valueOf(deldate.getYear());
	}
	
	
	public String addDate(BotFinInstHolidaysData data){
		
		LinkedList<BotFinInstHolidaysData> evaHoliList = new LinkedList<>();
		
		evaHoliList.add(data);
		
		LocalDate adddate = LocalDate.parse(data.getDate()
				,DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD));
		
		File holiFile = new File(resource+adddate.getYear()+".txt");
		
		List<String> evaholidays = null;
		if(holiFile.exists()) {
			try {
				evaholidays = FileUtils.readLines(holiFile,StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		if(evaholidays != null && !evaholidays.isEmpty()) {
			for(String evaholiday : evaholidays) {
				String[] evaholiData = StringUtils.split(evaholiday, "[|]");
				if(evaholiData != null && evaholiData.length > 0 && validateFormat(evaholiData[0],adddate.getYear())) {
					BotFinInstHolidaysData bean = new BotFinInstHolidaysData();
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD);
					LocalDate localDate = LocalDate.parse(evaholiData[0],dtf);
					
					if(data.getDate().equals(StringUtils.trim(evaholiData[0]))) {
						return null;
					}
					
					bean.setDate(evaholiData[0]);
					bean.setHolidayWeekDayThai(localDate.getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("th")));
					if(evaholiData.length == 3) {
						bean.setHolidayDescriptionThai(evaholiData[1]);
						bean.setHolidayDescription(evaholiData[2]);
					}else {
						bean.setHolidayDescription(EVAConstants.BLANK);
						bean.setHolidayDescriptionThai(EVAConstants.BLANK);
					}
					evaHoliList.add(bean);
				}
			}
		}
		
		
		evaHoliList.sort((e1,e2)->{
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD);
			
			LocalDate date1 = LocalDate.parse(e1.getDate(),dtf);
			
			LocalDate date2 = LocalDate.parse(e2.getDate(),dtf);
			
            return date1.compareTo(date2);
			
		});
		
		
		
		LinkedList<BotFinInstHolidaysData> botHoliList = getBOTHolidays(adddate.getYear());
		List<String> newDatas = new ArrayList<>();
		for(BotFinInstHolidaysData evaHoli : evaHoliList) {
			StringJoiner joiner = new StringJoiner("|");
			for(BotFinInstHolidaysData botHoli : botHoliList) {
				if(botHoli.getDate().equalsIgnoreCase(evaHoli.getDate())) {
					BeanUtils.copyProperties(botHoli, evaHoli);
				}
			}
			joiner.add(evaHoli.getDate()).add(evaHoli.getHolidayDescriptionThai()).add(evaHoli.getHolidayDescription());
			newDatas.add(joiner.toString());
		}
		
		try {
			if(newDatas != null && !newDatas.isEmpty()) {
				FileUtils.writeLines(holiFile, newDatas);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return String.valueOf(adddate.getYear());
	}
	
	
	public LinkedList<BotFinInstHolidaysData> getHolidaysDataList(int year)
	{
		LinkedList<BotFinInstHolidaysData> evaHoliList = new LinkedList<>();
		
		File holiFile = new File(resource+year+".txt");
		
		List<String> evaholidays = null;
		try {
			if(holiFile.exists()) {
				evaholidays = FileUtils.readLines(holiFile,StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return evaHoliList;
		}
		
		if(evaholidays != null && !evaholidays.isEmpty()) {
			for(String evaholiday : evaholidays) {
				String[] evaholiData = StringUtils.split(evaholiday, "[|]");
				if(evaholiData != null && evaholiData.length > 0 && validateFormat(evaholiData[0],year)) {
					BotFinInstHolidaysData bean = new BotFinInstHolidaysData();
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(EVAConstants.DATE_FORMAT_YYYY_MM_DD);
					LocalDate localDate = LocalDate.parse(evaholiData[0],dtf);
					bean.setDate(evaholiData[0]);
					bean.setHolidayWeekDayThai(localDate.getDayOfWeek().getDisplayName(TextStyle.FULL,new Locale("th")));
					if(evaholiData.length == 3) {
						bean.setHolidayDescriptionThai(evaholiData[1]);
						bean.setHolidayDescription(evaholiData[2]);
					}else {
						bean.setHolidayDescription(EVAConstants.BLANK);
						bean.setHolidayDescriptionThai(EVAConstants.BLANK);
					}
					evaHoliList.add(bean);
				}
			}
		
		
			LinkedList<BotFinInstHolidaysData> botHoliList = getBOTHolidays(year);
			
			List<String> newDatas = new ArrayList<>();
			for(BotFinInstHolidaysData evaHoli : evaHoliList) {
				StringJoiner joiner = new StringJoiner("|");
				for(BotFinInstHolidaysData botHoli : botHoliList) {
					if(botHoli.getDate().equalsIgnoreCase(evaHoli.getDate())) {
						BeanUtils.copyProperties(botHoli, evaHoli);
					}
				}
				joiner.add(evaHoli.getDate()).add(evaHoli.getHolidayDescriptionThai()).add(evaHoli.getHolidayDescription());
				newDatas.add(joiner.toString());
			}
			try {
				if(newDatas != null && !newDatas.isEmpty()) {
					FileUtils.writeLines(holiFile, newDatas);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return getBOTHolidays(year);
			}
			return evaHoliList;
		}else {
			return evaHoliList;
		}
		
	}
	
	public boolean validateFormat(String date) {
		try {
			
			if(StringUtils.isBlank(date)){
				return false;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat(EVAConstants.DATE_FORMAT_YYYY_MM_DD);
			
			sdf.parse(date).toInstant()
				.atZone(ZoneId.of("Asia/Bangkok"))
				.toLocalDate();
			
		}catch(ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean validateFormat(String date,int year) {
		try {
			
			if(StringUtils.isBlank(date)){
				return false;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat(EVAConstants.DATE_FORMAT_YYYY_MM_DD);
			
			LocalDate localDate = sdf.parse(date).toInstant()
				.atZone(ZoneId.of("Asia/Bangkok"))
				.toLocalDate();
			
			int yearConv = localDate.getYear();
			
			if(year != yearConv) {
				return false;
			}
			
		}catch(ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public LinkedList<BotFinInstHolidaysData> getBOTHolidays(int year) {
		
		LinkedList<BotFinInstHolidaysData> data = new LinkedList<>();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("x-ibm-client-id", "0c1c8af3-e77f-419f-ab5c-e9b5cee7205b");
			headers.set("accept", "application/json");
	
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			
			ResponseEntity<BotRootResp> resp = restTemplate.exchange("https://apigw1.bot.or.th/bot/public/financial-institutions-holidays/?year="+year
					,HttpMethod.GET
					,entity
					,BotRootResp.class);
			
			if(HttpStatus.OK.equals(resp.getStatusCode())) {
				data = resp.getBody().getResult().getData();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
		
	}
	
}
