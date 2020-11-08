package com.naotakapp.sprtest.boottest01.backend;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ret_json01 {

    @RequestMapping("/retJson01")
    public void retJson01( HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");
        res.getWriter().write("<html><head><title>Return JSON 01</title></head>JSON文字列を返す予定</html>");

        System.out.println("retJson01 : debug log.");
    }
}
