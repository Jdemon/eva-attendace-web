package com.eva.hr.report.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eva.hr.report.bot.api.BotFinInstHolidaysData;
import com.eva.hr.report.service.HolidayService;

@Controller
@RequestMapping("/eva")
public class HolidayController {
	
	@Autowired
	private HolidayService holidayService;
	
	@GetMapping(path = "/holiday")
	public ModelAndView getHoliday(@RequestParam(name="year",required=false) String yearStr) throws IOException {
		LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Bangkok"));
		int year = localDate.getYear();
		if(StringUtils.isNotBlank(yearStr)) {
			year =Integer.parseInt(yearStr);
		}
		LinkedList<BotFinInstHolidaysData> data = holidayService.getHolidaysDataList(year);
		
		ModelAndView modelAndView = new ModelAndView("eva/holiday");
		modelAndView.addObject("dataset", data);
		modelAndView.addObject("yearSelected",String.valueOf(year));
		modelAndView.addObject("year1",String.valueOf(localDate.getYear()));
		modelAndView.addObject("year2",String.valueOf(localDate.getYear()-1));
		modelAndView.addObject("year3",String.valueOf(localDate.getYear()-2));
		return modelAndView;
		
	}
	
	
	@GetMapping(path = "/holiday/del")
	public ModelAndView delHoliday(@RequestParam("date") String date) throws IOException {
		
		ModelAndView modelAndView = null;
		if(holidayService.validateFormat(date)) {
			
			String year = holidayService.deldate(date);
			
			if(StringUtils.isNotBlank(year)) {
				modelAndView = new ModelAndView("eva/return");
				modelAndView.addObject("value",year);
			}else {
				modelAndView = new ModelAndView("eva/return");
				modelAndView.addObject("value","Error occurred: can not delete date.");
			}
			
		}else {
			modelAndView = new ModelAndView("eva/return");
			modelAndView.addObject("value","Error occurred: can not delete date.");
		}
		return modelAndView;
	}
	
	@GetMapping(path = "/holiday/add")
	public ModelAndView addHoliday(@RequestParam("date") String date,@RequestParam("desc") String desc
			,@RequestParam("descTh") String descTh) throws IOException {
		
		ModelAndView modelAndView = null;
		if(holidayService.validateFormat(date)) {
		
			BotFinInstHolidaysData data = new BotFinInstHolidaysData();
			data.setDate(date);
			data.setHolidayDescription(desc);
			data.setHolidayDescriptionThai(descTh);
			
			String year = holidayService.addDate(data);
			
			if(StringUtils.isNotBlank(year)) {
				modelAndView = new ModelAndView("eva/return");
				modelAndView.addObject("value",year);
			}else {
				modelAndView = new ModelAndView("eva/return");
				modelAndView.addObject("value","Error occurred: can not add date.");
			}
			
		}else {
			modelAndView = new ModelAndView("eva/return");
			modelAndView.addObject("value","Error occurred: can not add date.");
		}
		return modelAndView;
	}

}
