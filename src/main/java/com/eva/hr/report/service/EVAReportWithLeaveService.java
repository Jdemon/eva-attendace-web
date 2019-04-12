package com.eva.hr.report.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eva.hr.report.bean.EVADataConvertBean;
import com.eva.hr.report.conts.EVAConstants;
import com.eva.hr.report.process.ConvertDataProcess;


@Service
public class EVAReportWithLeaveService {
	
	@Autowired
	private ConvertDataProcess convertDataProcess;
	
	@Value("${temp.path}")
	private String temppath;
	
	@Value("${temp.file.delflag}")
	private String delflag;
	
	@Value("${resource.path}")
	private String resource;
	
	@Value("${download.path}")
	private String downloadpath;
	
	public String getDwPath(String filename) {
		StringBuilder builder = new StringBuilder(downloadpath);
		builder.append(filename).append(".xlsx");
		return builder.toString();
	}
	
	
	public byte[] downloadFile(String filename) throws IOException {
		byte[] fileData = FileUtils.readFileToByteArray(new File(getDwPath(filename))).clone();
		FileUtils.deleteQuietly(new File(getDwPath(filename)));
		return fileData;
	}
	
	public String saveAttendance(MultipartFile multipartFile) throws Exception {
		
		File file = convert(multipartFile);
		
		validateFile(file, "csv");
		
		return FilenameUtils.getName(file.getName());
		
	}
	
	private String convertCSVToExcel(File file) throws Exception {
		
		List<String> fileData = FileUtils.readLines(file,EVAConstants.UTF_8);
		
		FileUtils.deleteQuietly(file);
		
		fileData = convertDataProcess.processStepOne(fileData);
		
		List<EVADataConvertBean> convertBean = convertDataProcess.processStepTwo(fileData);
		
		fileData = convertDataProcess.processStepThree(convertBean, fileData);
		
		String name = FilenameUtils.getBaseName(file.getName());
		
		String tempFilePath = convertDataProcess.processStepFour(fileData,name);
		
		byte[] data = convertDataProcess.generateReport(resource+"EVAReport.jasper", tempFilePath);
		
		FileUtils.writeByteArrayToFile(new File(getDwPath(name)), data);
		
		if("Y".equals(delflag)) {
			try {FileUtils.deleteQuietly(new File(tempFilePath));}catch(Exception ignore) {ignore.printStackTrace();};
		}
		
		return name+".xlsx";
	}
	
	
	
	private boolean validateFile(File file,String extension) throws Exception {
		
		String oriExtension = FilenameUtils.getExtension(file.getName());
		if(StringUtils.trim(extension).equals(oriExtension)) {
			return true;
		}else {
			try {FileUtils.deleteQuietly(file);}catch(Exception ignore) {ignore.printStackTrace();};
			throw new Exception("Error file extension is not ."+extension);
		}
		
	}
	
	public File convert(MultipartFile file) throws IOException
	{    
		File convFile = new File(temppath);
		if(!convFile.exists()) {
			convFile.mkdirs();
		}
	    convFile = new File(temppath+file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}

}
