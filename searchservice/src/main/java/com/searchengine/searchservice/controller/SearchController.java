package com.searchengine.searchservice.controller;

import com.searchengine.searchservice.dto.SearchRequestParameters;
import com.searchengine.searchservice.dto.SearchResponse;
import com.searchengine.searchservice.dto.SuggestionRequestParameters;
import com.searchengine.searchservice.service.SearchService;
import com.searchengine.searchservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SuggestionService suggestionService;
    private final SearchService searchService;

    @GetMapping("/api/suggestions")
    public List<String> suggest(SuggestionRequestParameters parameters) {
        return suggestionService.fetchSuggestions(parameters);
    }

    @GetMapping("/api/search")
    public SearchResponse search(SearchRequestParameters parameters){
        return searchService.search(parameters);
    }

}
