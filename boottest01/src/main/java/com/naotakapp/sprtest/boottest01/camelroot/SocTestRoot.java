package com.naotakapp.sprtest.boottest01.camelroot;

import com.naotakapp.sprtest.boottest01.camelprocess.SocTestProcess;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SocTestRoot extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("soctest://hogehoge")
            .process( new SocTestProcess() )
            .to("soctest://mogamoga");
    }
}

