package com.searchengine.searchservice.dto;

import jakarta.validation.constraints.NotNull;

public record SearchRequestParameters(
        String query,
        String distance,
        Double latitude,
        Double longitude,
        Double rating,
        String state,
        String offerings,
        @NotNull(message = "page field is required")
        Integer page,
        @NotNull(message = "size field is required")
        Integer size
) {
}
