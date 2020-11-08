package com.naotakapp.sprtest.boottest01.camelprocess;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class bodyappend implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        if( null != body ){
            body = "testpage01";
        } else {
            body = "resultpage01"; // + body;
        }
        exchange.getIn().setBody(body);
    }

}
