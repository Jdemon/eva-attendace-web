package com.eva.hr.report.bot.api;

import java.io.Serializable;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"api","timestamp","data"})
public class BotFinInstHolidaysResp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@JsonProperty("api")
	private String api;
	@JsonProperty("timestamp")
	private String timestamp;
	@JsonProperty("data")
	private LinkedList<BotFinInstHolidaysData> data;

}
