package com.searchengine.searchservice.service.impl;

import com.searchengine.searchservice.dto.*;
import com.searchengine.searchservice.service.SearchService;
import com.searchengine.searchservice.util.Constants;
import com.searchengine.searchservice.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public SearchResponse search(SearchRequestParameters parameters) {
        logger.info("search request: {}", parameters);
        NativeQuery query = NativeQueryBuilder.toSearchQuery(parameters);
        logger.info("bool query: {}", query.getQuery());

        SearchHits<Business> searchHits = elasticsearchOperations.search(query, Business.class, Constants.Index.BUSINESS);
        return buildResponse(parameters, searchHits);
    }

    private SearchResponse buildResponse(SearchRequestParameters parameters, SearchHits<Business> searchHits) {
        List<Business> results = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        SearchPage<Business> searchPage = SearchHitSupport.searchPageFor(searchHits, PageRequest.of(parameters.page(), parameters.size()));
        Pagination pagination = new Pagination(
                searchPage.getNumber(),
                searchPage.getNumberOfElements(),
                searchPage.getTotalElements(),
                searchPage.getTotalPages()
        );

        return new SearchResponse(
                results,
                pagination,
                searchHits.getExecutionDuration().toMillis()
        );
    }
}
