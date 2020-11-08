package com.naotakapp.sprtest.boottest01.camelprocess;

import java.util.Calendar;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DebugLogEnd implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Calendar calendar = Calendar.getInstance();
        System.out.println( "DEBUG LOG [" + calendar.getTime() + "] : ルートが終了しました" );
        System.out.println( "HEADDER : [" + exchange.getIn().getHeader( Exchange.CREATED_TIMESTAMP ) + "]" );
    }

}
