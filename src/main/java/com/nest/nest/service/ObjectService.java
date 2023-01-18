package com.nest.nest.service;

import com.nest.nest.model.NestApiConstant;
import com.nest.nest.model.ObjectMetaResponseDto;
import com.nest.nest.model.ObjectResponseDto;
import com.nest.nest.repository.MinioRepository;
import io.minio.MinioClient;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjectService {

  private final MinioClient minioclient;
  private final MinioRepository minioRepository;

  public ObjectMetaResponseDto getObjectMetaResponseDto(String messageIdentifiers) {
    var messageIdentifierList =
        Arrays.asList(messageIdentifiers.split(NestApiConstant.MESSAGE_IDENTIFIER_SEPARATOR));
    return new ObjectMetaResponseDto();
  }

  public ObjectResponseDto getObjectResponseDto(String objectId) {
    return new ObjectResponseDto();
  }
}
