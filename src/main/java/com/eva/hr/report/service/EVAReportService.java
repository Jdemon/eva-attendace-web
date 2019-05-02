package com.eva.hr.report.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
public class EVAReportService {

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
		builder.append(filename);
		return builder.toString();
	}

	public byte[] downloadFile(String filename) throws IOException {
		byte[] fileData = FileUtils.readFileToByteArray(new File(getDwPath(filename))).clone();
		FileUtils.deleteQuietly(new File(getDwPath(filename)));
		return fileData;
	}

	public String execute(MultipartFile multipartFile) throws Exception {

		File file = convert(multipartFile);

		validateFile(file, "csv");

		return convertCSVToExcel(file);

	}

	public String executeZip(MultipartFile multipartFile) throws Exception {
		File file = convert(multipartFile);

		validateFile(file, "zip");

		return convertZip(file);

	}

	private String convertZip(File file) throws Exception {
		
		String upzipDir = temppath+FilenameUtils.getBaseName(file.getName());
		
		unzip(file, upzipDir);
		
		FileUtils.deleteQuietly(file);
		
		File tempDir = new File(upzipDir);
		
		File[] csvList = tempDir.listFiles();
		List<String> list = new ArrayList<>();
		if(csvList != null && csvList.length > 0) {
			
			for(File csv : csvList) {
				validateFile(csv, "csv");
				
				list.add(convertCSVToExcel(csv));
			}
		}
		
		String zipDir = downloadpath+FilenameUtils.getBaseName(file.getName())+".zip";
		
		zip(list, zipDir);
		
		return FilenameUtils.getBaseName(file.getName())+".zip";
	}
	
	public void zip(List<String> srcFiles,String zipname) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipname);
				ZipOutputStream zipOut = new ZipOutputStream(fos);) {
			for (String srcFile : srcFiles) {
				File fileToZip = new File(downloadpath+srcFile);
				try (FileInputStream fis = new FileInputStream(fileToZip);) {
					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zipOut.write(bytes, 0, length);
					}
				}
				FileUtils.deleteQuietly(fileToZip);
			}
		}
	}

	private void unzip(File file, String destDir) {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(file); 
				ZipInputStream zis = new ZipInputStream(fis);) {
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				try (FileOutputStream fos = new FileOutputStream(newFile);) {
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
				}
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();

			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String convertCSVToExcel(File file) throws Exception {

		List<String> fileData = FileUtils.readLines(file, EVAConstants.UTF_8);

		FileUtils.deleteQuietly(file);

		fileData = convertDataProcess.processStepOne(fileData);

		List<EVADataConvertBean> convertBean = convertDataProcess.processStepTwo(fileData);

		fileData = convertDataProcess.processStepThree(convertBean, fileData);

		String name = FilenameUtils.getBaseName(file.getName());

		String tempFilePath = convertDataProcess.processStepFour(fileData, name);

		byte[] data = convertDataProcess.generateReport(resource + "EVAReport.jasper", tempFilePath);

		FileUtils.writeByteArrayToFile(new File(getDwPath(name+".xlsx")), data);

		if ("Y".equals(delflag)) {
			try {
				FileUtils.deleteQuietly(new File(tempFilePath));
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
			;
		}

		return name + ".xlsx";
	}

	private boolean validateFile(File file, String extension) throws Exception {

		String oriExtension = FilenameUtils.getExtension(file.getName());
		if (StringUtils.trim(extension).equals(oriExtension)) {
			return true;
		} else {
			try {
				FileUtils.deleteQuietly(file);
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
			;
			throw new Exception("Error file extension is not ." + extension);
		}

	}

	public File convert(MultipartFile file) throws IOException {
		File convFile = new File(temppath);
		if (!convFile.exists()) {
			convFile.mkdirs();
		}
		convFile = new File(temppath + file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
