package com.naotakapp.sprtest.boottest01.camelcontroller;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test_takamoroot01 {

    @Autowired
    private CamelContext camelContext;

    @RequestMapping("/app_takamoroot01")
    @ResponseBody
    public String app_takamoroot01() {

        // camelで流通するデータの作成
        Exchange exchange = new DefaultExchange(camelContext);

        // routeにデータを送信
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.send("direct:takamoroot01", exchange);

        // camelのrouteが例外で終了したかどうかの確認
        if (exchange.getException() != null) {
            return "NG";
        }
        String ret = exchange.getIn().getBody(String.class);
        return ret; //exchange.getIn().getBody(String.class);
    }
}

