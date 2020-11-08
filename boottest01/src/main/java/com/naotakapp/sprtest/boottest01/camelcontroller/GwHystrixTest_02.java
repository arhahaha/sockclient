package com.naotakapp.sprtest.boottest01.camelcontroller;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.util.StopWatch;

@Controller
public class GwHystrixTest_02 {

    @Autowired
    private CamelContext camelContext;

    @RequestMapping("/gw_hystrix_test02")
    @ResponseBody
    public String GwHystrixTest02_svc() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // camelで流通するデータの作成
        Exchange exchange = new DefaultExchange(camelContext);

        // routeにデータを送信
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.send("direct:timeout_root_10sec", exchange);

        stopWatch.stop();
        System.out.println( "[/gw_hystrix_test02] 処理時間(ms)：" + stopWatch.getTotalTimeMillis() );

        // camelのrouteが例外で終了したかどうかの確認
        if (exchange.getException() != null) {
            return "NG (10sec): [" + exchange.getException().getClass() + "]" + exchange.getException().getMessage();
        }
        String ret = exchange.getIn().getBody(String.class);
        return ret; //exchange.getIn().getBody(String.class);
    }
}

