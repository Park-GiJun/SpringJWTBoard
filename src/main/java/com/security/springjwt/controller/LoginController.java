package com.security.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String mainP () {
		return "/LoginPage/Login";
	}
}