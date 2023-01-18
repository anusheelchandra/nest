package com.nest.nest.model;

import java.util.List;
import java.util.Map;

public record ObjectMetaResponseDto() {

  private static Map<String, List<Map<String, String>>> objectMetaByMessageIdentifier;
}
