package com.naotakapp.sprtest.boottest01.camelroot;

import com.naotakapp.sprtest.boottest01.camelprocess.TransResponse;
import com.naotakapp.sprtest.boottest01.camelprocess.bodyappend;
import com.naotakapp.sprtest.boottest01.camelprocess.DebugLogEnd;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
//import com.netflix.hystrix.config.HystrixConfiguration;

@Component
public class gw_timeout_root10sec extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:timeout_root_10sec")
            .circuitBreaker()
                .hystrixConfiguration()
                    .executionTimeoutInMilliseconds(10000)
            .end()

            .log( "DEBUG LOG(10秒ルート) : ルートを開始しました" )
            .process( new bodyappend())
            .to("http://localhost:8080/stub_sleep30")
            .process( new TransResponse() )
            .process( new DebugLogEnd() )
            .log( "DEBUG LOG(10秒ルート) : ルートを終了しました" );
    }
}
