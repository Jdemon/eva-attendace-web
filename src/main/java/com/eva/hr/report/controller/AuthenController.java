package com.eva.hr.report.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenController {

	@GetMapping({"/","/login"})
	public String root() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null && auth.isAuthenticated() && !(auth 
		          instanceof AnonymousAuthenticationToken) ){
			 return "eva/index";
		}
		
		return "login";
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "/error/access-denied";
	}

}
