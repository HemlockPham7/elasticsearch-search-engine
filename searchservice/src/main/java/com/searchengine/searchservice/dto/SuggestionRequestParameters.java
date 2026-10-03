package com.searchengine.searchservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SuggestionRequestParameters(
        @NotBlank(message = "Prefix field cannot be blank")
        String prefix,
        @Min(value = 5, message = "Limit cannot be smaller than 5")
        Integer limit
) {
}
