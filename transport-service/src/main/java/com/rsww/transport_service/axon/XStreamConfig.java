package com.rsww.transport_service.axon;

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
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.allowTypesByWildcard(new String[] {
            "com.rsww.*"
        });
        return xstream;
    }
}
