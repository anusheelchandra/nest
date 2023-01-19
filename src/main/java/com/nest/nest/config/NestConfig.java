package com.nest.nest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
@ConfigurationProperties(prefix = "nest.config")
@Getter
@Setter
public class NestConfig {

    private String corsAllowedOrigins;

    public String[] getCorsAllowedOriginsList() {
        return this.getCorsAllowedOrigins().split(Pattern.quote(";"));
    }
}
