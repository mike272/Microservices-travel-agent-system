package com.rsww.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;


@Configuration
public class XStreamConfig
{

    @Bean
    public XStream xstreamSetup()
    {
        final XStream xstream = new XStream();
        // clear out existing permissions and set own ones
        xstream.addPermission(AnyTypePermission.ANY);
        // allow some specific classes
//        xstream.allowTypes();
        // allow any class from certain packages
        xstream.allowTypesByWildcard(new String[] {
            "com.rsww.*"
        });
        return xstream;
    }
}
