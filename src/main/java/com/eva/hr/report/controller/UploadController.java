package com.eva.hr.report.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eva.hr.report.service.EVAReportService;

@Controller
@RequestMapping("/eva")
public class UploadController {

	@Autowired
	private EVAReportService evaReportService;

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public String evaIndex() {
		return "eva/index";
	}

	@PostMapping(path = "/upload")
	public ModelAndView uploadfile(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file) throws Exception {
		ModelAndView modelAndView;
		try {
			String filename = evaReportService.execute(file);
			
			modelAndView = new ModelAndView("eva/dwn-page");
			modelAndView.addObject("filename", filename);
		}catch(Exception e) {
			e.printStackTrace();
			modelAndView = new ModelAndView("error/error");
			modelAndView.addObject("errMsg",e.getMessage());
		}
		
		return modelAndView;
	}

	@GetMapping(path = "/download/{name}")
	public void downloadFile(HttpServletResponse response, @PathVariable("name") String filename) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.addHeader("Content-Disposition", "attachment; filename=" + filename+".xlsx");
		response.getOutputStream().write(evaReportService.downloadFile(filename));
		response.getOutputStream().close();
		response.getOutputStream().flush();
	}
}
