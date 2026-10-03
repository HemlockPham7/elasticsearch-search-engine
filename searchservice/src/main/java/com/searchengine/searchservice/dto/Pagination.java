package com.searchengine.searchservice.dto;

public record Pagination(int page,
                         int size,
                         long totalElements,
                         int totalPages) {
}