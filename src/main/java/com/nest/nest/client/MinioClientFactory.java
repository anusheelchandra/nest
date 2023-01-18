package com.nest.nest.client;

import com.nest.nest.config.MinioConfig;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioClientFactory {

  private final MinioConfig minioConfig;

  @Bean
  public MinioClient minioclient() {
    return MinioClient.builder()
        .endpoint(minioConfig.getEndpoint())
        .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
        .build();
  }
}
