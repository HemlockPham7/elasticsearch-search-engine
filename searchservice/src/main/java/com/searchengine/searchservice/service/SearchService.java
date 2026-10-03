package com.searchengine.searchservice.service;

import com.searchengine.searchservice.dto.SearchRequestParameters;
import com.searchengine.searchservice.dto.SearchResponse;

public interface SearchService {

    SearchResponse search(SearchRequestParameters parameters);
}
