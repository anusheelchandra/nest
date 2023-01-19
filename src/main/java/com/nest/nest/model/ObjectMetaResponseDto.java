package com.nest.nest.model;

import java.util.List;
import java.util.Map;

public record ObjectMetaResponseDto(Map<String, List<String>> metadataByIdentifiers) {

  private static Map<String, List<String>> objectMetaByMessageIdentifier;
}
