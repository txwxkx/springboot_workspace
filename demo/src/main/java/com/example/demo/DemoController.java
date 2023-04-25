package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//http://localhost:8090/

@Controller
public class DemoController {
	@ResponseBody
	@RequestMapping("/")
	public String home() {
		System.out.println("Hello boot!!!");
		System.out.println("program");
		return "Hello boot!!";
	}

}
