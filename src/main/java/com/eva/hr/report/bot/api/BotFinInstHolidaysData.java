package com.eva.hr.report.bot.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"HolidayWeekDay"
	,"HolidayWeekDayThai"
	,"Date"
	,"DateThai"
	,"HolidayDescription"
	,"HolidayDescriptionThai"})
public class BotFinInstHolidaysData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@JsonProperty("HolidayWeekDay")
	private String HolidayWeekDay;
	@JsonProperty("HolidayWeekDayThai")
	private String HolidayWeekDayThai;
	@JsonProperty("Date")
	private String Date;
	@JsonProperty("DateThai")
	private String DateThai;
	@JsonProperty("HolidayDescription")
	private String HolidayDescription;
	@JsonProperty("HolidayDescriptionThai")
    private String HolidayDescriptionThai;

}
