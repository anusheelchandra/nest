package com.nest.nest.config;

import io.minio.MinioClient;
import lombok.Getter;
<<<<<<< src/main/java/com/nest/nest/config/MinioConfig.java
import org.springframework.boot.context.properties.ConfigurationProperties;
=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
>>>>>>> src/main/java/com/nest/nest/config/MinioConfig.java
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MinioConfig {

  @Value("${minio.accessKey}")
  private String accessKey;

  @Value("${minio.secretKey}")
  private String secretKey;

  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.bucketName}")
  private String bucketName;

  @Bean
  public MinioClient minioclient() {
    return MinioClient.builder()
        .endpoint(this.getEndpoint())
        .credentials(this.getAccessKey(), this.getSecretKey())
        .build();
  }
}
