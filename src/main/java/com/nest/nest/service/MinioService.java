package com.nest.nest.service;

import com.nest.nest.config.MinioConfig;
import com.nest.nest.model.NestApiConstant;
import com.nest.nest.model.ObjectMetaResponseDto;
import com.nest.nest.repository.MinioRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioService {

  private final MinioRepository minioRepository;
  private final MinioConfig minioConfig;

  public ObjectMetaResponseDto getObjectMetaResponseDto(List<String> messageIdentifiers) {
    var identifiers =
        messageIdentifiers.stream()
            .map(identifier -> identifier + NestApiConstant.TEXT_FILE_SUFFIX)
            .collect(Collectors.toList());
    return new ObjectMetaResponseDto(
        minioRepository.getMetadataByIdentifiers(minioConfig.getBucketName(), identifiers));
    // return new ObjectMetaResponseDto(Map.of(messageIdentifiers.get(0),
    // List.of("BA0073800000XDBW-tnt-Invoice.pdf")));
  }

  public byte[] getObject(String objectId) {
    try {
      return IOUtils.toByteArray(
          minioRepository.getObjectById(minioConfig.getBucketName(), objectId).getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new byte[0];
  }

  public String saveObjectReference(String identifier, String names) {
    var nameList =
        Arrays.stream(names.split(NestApiConstant.MESSAGE_IDENTIFIER_SEPARATOR))
            .map(name -> identifier + NestApiConstant.MESSAGE_CONTENT_SEPARATOR + name)
            .collect(Collectors.toList());
    var result =
        minioRepository.saveObjectReference(identifier, nameList, minioConfig.getBucketName());
    return result.object();
  }

  public String saveObject(MultipartFile multipartFile) {
    var result =
        minioRepository.saveObject(
            multipartFile,
            multipartFile.getOriginalFilename(),
            MediaType.APPLICATION_PDF_VALUE,
            minioConfig.getBucketName());
    return result.object();
  }
}
