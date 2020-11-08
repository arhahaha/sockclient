package com.naotakapp.sprtest.boottest01.camelcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class test01 {

    @RequestMapping("/app01")
    public String app01() {
        return "testpage01";
    }
}