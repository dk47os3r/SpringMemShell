package com.example.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {
    @RequestMapping("/api")
    @ResponseBody
    public String scan(){
        return "ok";
    }
}
