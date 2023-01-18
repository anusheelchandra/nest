package com.nest.nest.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

  private String accessKey;
  private String secretKey;
  private String endpoint;
}
