package org.zucc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@ResponseBody
@CrossOrigin
public class HelloController {
    @GetMapping("/1")
    public String getString() {
        return "Hello!";
    }
}
