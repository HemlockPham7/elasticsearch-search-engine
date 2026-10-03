package com.searchengine.searchservice.service;

import com.searchengine.searchservice.dto.SuggestionRequestParameters;

import java.util.List;

public interface SuggestionService {

    List<String> fetchSuggestions(SuggestionRequestParameters parameters);
}
