package com.searchengine.searchservice.controller;

import com.searchengine.searchservice.dto.SuggestionRequestParameters;
import com.searchengine.searchservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SuggestionService suggestionService;

    @GetMapping("/api/suggestions")
    public List<String> suggest(SuggestionRequestParameters parameters) {
        return suggestionService.fetchSuggestions(parameters);
    }
}
