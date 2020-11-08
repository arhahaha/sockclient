package com.naotakapp.sprtest.boottest01.backend;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StubSleep30 {

    @RequestMapping("/stub_sleep30")
    public void StubAPI_Sleep30( final HttpServletResponse res) throws IOException {
        try {
            System.out.println("BACKEND：バックエンドAPIがリクエストを受信しました.");
            System.out.println("BACKEND：30秒スリープします.");
            TimeUnit.SECONDS.sleep(30);
        } catch (final InterruptedException e) {
            System.out.println("BACKEND：スリープでエラーが発生しました.");
        }
        System.out.println("BACKEND：スリープが完了しました.");
        res.sendError( HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED );
    }
}
