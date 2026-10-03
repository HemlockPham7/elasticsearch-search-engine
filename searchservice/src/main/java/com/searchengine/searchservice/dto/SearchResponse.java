package com.searchengine.searchservice.dto;

import java.util.List;

public record SearchResponse(List<Business> results,
                             Pagination pagination,
                             long timeTaken) {
}