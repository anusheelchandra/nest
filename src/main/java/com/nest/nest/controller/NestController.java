package com.nest.nest.controller;

import com.nest.nest.model.ObjectMetaResponseDto;
import com.nest.nest.model.ObjectResponseDto;
import javax.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/nest")
public class NestController {

  @GetMapping(
      value = "/objectmeta/{messageIdentifiers}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ObjectMetaResponseDto> getObjectMetadata(
      @NotBlank @PathVariable("messageIdentifiers") String messageIdentifiers) {
    return new ResponseEntity<>(new ObjectMetaResponseDto(), HttpStatus.OK);
  }

  @GetMapping(value = "/object/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ObjectResponseDto> getObject(
      @NotBlank @PathVariable("objectId") String objectId) {
    return new ResponseEntity<>(new ObjectResponseDto(), HttpStatus.OK);
  }
}
