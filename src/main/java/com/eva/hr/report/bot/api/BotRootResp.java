package com.eva.hr.report.bot.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class BotRootResp  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private BotFinInstHolidaysResp result;

}
