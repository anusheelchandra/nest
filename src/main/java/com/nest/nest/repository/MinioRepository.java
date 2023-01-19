package com.nest.nest.repository;

import com.nest.nest.model.NestApiConstant;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioRepository {

  private final MinioClient minioClient;

  public Map<String, List<String>> listBuckets() {
    try {
      minioClient.listBuckets();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return Collections.emptyMap();
  }

  public Map<String, List<String>> getMetadataByIdentifiers(
      String bucketName, List<String> identifiers) {
    var ids = identifiers.stream().map(s -> s + "/").collect(Collectors.toList());
    var results =
        minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .includeUserMetadata(true)
                .recursive(true)
                // .extraQueryParams(Map.of(NestApiConstant.OBJECT_ATTACHMENT_REFERENCE,
                // NestApiConstant.OBJECT_ATTACHMENT_REFERENCE))
                .build());
    Map<String, List<String>> attachmentNameByIdentifier = new HashMap<>();

      for (var id: ids) {
        List<String> names = new ArrayList<>();
        for (Result<Item> item : results) {
          try {
            var object = item.get();
            if (object.objectName().contains(id) && object.objectName().contains(".")) {
              names.add(object.objectName());
            }
          } catch (Exception exception) {
            log.error(
                    String.format(
                            "Error fetching metadata for bucket : %s and identifier : %s",
                            bucketName, identifiers.toString()));
            log.error(exception.getMessage());
          }
        }
        if (!names.isEmpty()) {
          attachmentNameByIdentifier.put(id.replace("/", ""), names);
        }
      }
    return attachmentNameByIdentifier;
  }

  public InputStreamResource getObjectReferenceByIdentifier(String bucketName, String identifier) {
    return getObjectById(bucketName, identifier);
  }

  public InputStreamResource getObjectById(String bucketName, String objectId) {
    InputStreamResource inputStreamResource = null;
    try {
      if (bucketExists(bucketName))
        inputStreamResource =
            new InputStreamResource(
                minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(objectId).build()));
    } catch (Exception exception) {
      log.error(
          String.format(
              "Error while fetching object for bucket : %s and objectId : %s",
              bucketName, objectId));
    }
    return inputStreamResource;
  }

  public ObjectWriteResponse saveObject(
      MultipartFile objectFilePart, String metadata, String contentType, String bucketName) {
    ObjectWriteResponse objectWriteResponse = null;
    try {
      if (bucketExists(bucketName))
        objectWriteResponse =
            minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).contentType(contentType).stream(
                        objectFilePart.getInputStream(),
                        objectFilePart.getSize(),
                        NestApiConstant.OBJECT_PART_SIZE)
                    .object(objectFilePart.getOriginalFilename())
                    .headers(Map.of(NestApiConstant.OBJECT_METADATA, metadata))
                    // .userMetadata(Map.of(NestApiConstant.OBJECT_METADATA, metadata))
                    .build());
    } catch (Exception exception) {
      log.error(
          String.format(
              "Error while saving object for bucket : %s and metadata : %s", bucketName, metadata));
    }
    return objectWriteResponse;
  }

  public ObjectWriteResponse saveObjectReference(
      String identifier, List<String> attachmentNames, String bucketName) {
    ObjectWriteResponse objectWriteResponse = null;
    try {
      if (bucketExists(bucketName)) {
        var attachmentNamesString =
            String.join(NestApiConstant.MESSAGE_IDENTIFIER_SEPARATOR, attachmentNames);
        var tempFileStream = tempFileStream(identifier, attachmentNamesString);
        objectWriteResponse =
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .contentType(MediaType.TEXT_PLAIN_VALUE)
                    // .object(String.format("%s%s", identifier, NestApiConstant.TEXT_FILE_SUFFIX))
                    .object(String.format("%s%s", identifier, "/"))
                    .stream(
                        tempFileStream,
                        tempFileStream.available(),
                        NestApiConstant.OBJECT_PART_SIZE)
                    .userMetadata(
                        Map.of(NestApiConstant.OBJECT_ATTACHMENT_NAMES, attachmentNamesString))
                    .extraQueryParams(
                        Map.of(NestApiConstant.OBJECT_ATTACHMENT_NAMES, attachmentNamesString))
                    .build());
      }
    } catch (Exception exception) {
      log.error(
          String.format(
              "Error while saving references for bucket : %s , identifier : %s and attachmentNames"
                  + " : %s",
              bucketName, identifier, attachmentNames));
    }
    return objectWriteResponse;
  }

  private Boolean bucketExists(String bucketName) {
    var result = false;
    try {
      result = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    } catch (Exception exception) {
      log.error(String.format("Error while checking bucketExists for name : %s", bucketName));
    }
    return result;
  }

  private InputStream tempFileStream(String identifier, String attachmentNames) throws IOException {
    List<InputStream> streams =
        List.of(new ByteArrayInputStream(attachmentNames.getBytes(StandardCharsets.UTF_8)));
    return new SequenceInputStream(Collections.enumeration(streams));
  }
}
