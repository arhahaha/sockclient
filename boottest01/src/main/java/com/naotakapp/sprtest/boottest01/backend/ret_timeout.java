package com.naotakapp.sprtest.boottest01.backend;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ret_timeout {

    @RequestMapping("/retTimeout")
    public void retTimeout( HttpServletResponse res) throws IOException {
        res.sendError( HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED );
    }
}
