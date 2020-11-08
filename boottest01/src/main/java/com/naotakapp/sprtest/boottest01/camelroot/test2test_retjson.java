package com.naotakapp.sprtest.boottest01.camelroot;

import com.naotakapp.sprtest.boottest01.camelprocess.TransResponse;
import com.naotakapp.sprtest.boottest01.camelprocess.bodyappend;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class test2test_retjson extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:takamoroot01")
          .process( new bodyappend())
           //.to( "file:D:/Temp/test.log" )
          .to("http://localhost:8080/retJson01")
                //.setBody(constant("hello takamoto."));
          .process( new TransResponse() ); 
    }
}
