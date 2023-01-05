package com.sustech.ooad.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "front-static")
public class StaticProp {
    private String staticDirectory;
    private String staticUrl;
}
