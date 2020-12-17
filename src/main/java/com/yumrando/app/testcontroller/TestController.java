package com.yumrando.app.testcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test")
    public String indexTest(){
        return "user/profile";
    }

}
