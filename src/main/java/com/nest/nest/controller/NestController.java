package com.nest.nest.controller;

import com.nest.nest.model.ObjectMetaResponseDto;
import com.nest.nest.service.MinioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/nest")
@RequiredArgsConstructor
public class NestController {

  private final MinioService minioService;

  @GetMapping(
      value = "/objectmeta/{messageIdentifiers}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ObjectMetaResponseDto> getObjectMetadata(
      @PathVariable("messageIdentifiers") List<String> messageIdentifiers) {
    return new ResponseEntity<>(
        minioService.getObjectMetaResponseDto(messageIdentifiers), HttpStatus.OK);
  }

  @GetMapping(value = "/object/{identifier}/{objectId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getObject(@PathVariable("identifier") String identifier, @PathVariable("objectId") String objectId) {
    return new ResponseEntity<>(minioService.getObject(identifier + "/" + objectId), HttpStatus.OK);
  }

  @PostMapping(
      value = "/saveobject",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveObject(
      @RequestParam("multipartFile") MultipartFile multipartFile) {
    return new ResponseEntity<>(minioService.saveObject(multipartFile), HttpStatus.CREATED);
  }

  @PostMapping(
      value = "/saveobjectreference/{identifier}/{names}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveObjectReference(
      @PathVariable("identifier") String identifier, @PathVariable("names") String names) {
    return new ResponseEntity<>(
        minioService.saveObjectReference(identifier, names), HttpStatus.CREATED);
  }
}
