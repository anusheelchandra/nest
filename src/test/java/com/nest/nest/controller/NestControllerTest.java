package com.nest.nest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nest.nest.config.MinioConfig;
import com.nest.nest.repository.MinioRepository;
import com.nest.nest.service.MinioService;
import java.io.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class NestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private MinioService minioService;
  @Autowired private MinioRepository minioRepository;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private MinioConfig minioConfig;

  @Test
  public void shouldGetObjectMetadata() throws Exception {
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/nest/objectmeta/BA0073800000XDBW,CA0073800000XDBW,TEST")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    assertThat(mvcResult).isNotNull();
  }

  @Test
  public void shouldGetObject() throws Exception {
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/nest/object/BA0073800000XDBW/tnt-Invoice.pdf")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    assertThat(mvcResult).isNotNull();
  }

  @Test
  public void shouldSaveObjectReference() throws Exception {
    var mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(
                        "/api/nest/saveobjectreference/BA0073800000XDBW/tnt-Invoice.pdf")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    assertThat(mvcResult).isNotNull();
  }

  @Test
  public void shouldSaveObject() throws Exception {
    var file = createMultipartFile("BA0073800000XDBW-tnt-Invoice.pdf");
    var mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.multipart("/api/nest/saveobject").file(file))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    assertThat(mvcResult).isNotNull();
  }

  private MockMultipartFile createMultipartFile(String name) throws IOException {
    var file = new File("src/test/resources/temp/" + name);
    var stream = new FileInputStream(file);
    return new MockMultipartFile(
        "multipartFile", name, MediaType.MULTIPART_FORM_DATA_VALUE, stream);
  }
}
