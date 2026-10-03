package com.searchengine.searchservice.service.impl;

import com.searchengine.searchservice.dto.SuggestionRequestParameters;
import com.searchengine.searchservice.service.SuggestionService;
import com.searchengine.searchservice.util.Constants;
import com.searchengine.searchservice.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private static final Logger logger = LoggerFactory.getLogger(SuggestionServiceImpl.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public SuggestionServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        logger.info("Suggestion request: {}", parameters);
        NativeQuery query = NativeQueryBuilder.toSuggestQuery(parameters);
        SearchHits<Object> searchHits = elasticsearchOperations.search(query, Object.class, Constants.Index.SUGGESTION);
        return Optional.ofNullable(searchHits.getSuggest())
                .map(s -> s.getSuggestion(Constants.Suggestion.SUGGEST_NAME))
                .stream()
                .map(Suggest.Suggestion::getEntries)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry::getOptions)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry.Option::getText)
                .toList();
    }
}
