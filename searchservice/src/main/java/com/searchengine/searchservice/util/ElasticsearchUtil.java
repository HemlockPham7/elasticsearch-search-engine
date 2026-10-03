package com.searchengine.searchservice.util;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import java.util.List;
import java.util.function.UnaryOperator;

public class ElasticsearchUtil {

    public static Suggester buildCompleteSuggester(String suggestName, String field, String prefix, int limit) {
        SuggestFuzziness suggestFuzziness = SuggestFuzziness.of(builder -> builder.fuzziness(Constants.Fuzzy.LEVEL).prefixLength(Constants.Fuzzy.PREFIX_LENGTH));
        CompletionSuggester completionSuggester = CompletionSuggester.of(
                builder -> builder
                        .field(field)
                        .size(limit)
                        .fuzzy(suggestFuzziness)
                        .skipDuplicates(true)
        );
        FieldSuggester fieldSuggester = FieldSuggester.of(builder -> builder.prefix(prefix).completion(completionSuggester));
        return Suggester.of(builder -> builder.suggesters(suggestName, fieldSuggester));
    }

    public static Query buildTermQuery(String field, String value, float boost) {
        TermQuery termQuery = TermQuery.of(
                builder -> builder.field(field)
                        .value(value)
                        .boost(boost)
                        .caseInsensitive(true)
        );
        return Query.of(builder -> builder.term(termQuery));
    }

    public static Query buildRangeQuery(String field, UnaryOperator<NumberRangeQuery.Builder> function) {
        NumberRangeQuery numberRangeQuery = NumberRangeQuery.of(
                builder -> function.apply(builder.field(field))
        );
        RangeQuery rangeQuery = RangeQuery.of(
                builder -> builder.number(numberRangeQuery)
        );
        return Query.of(builder -> builder.range(rangeQuery));
    }

    public static Query buildGeoDistanceQuery(String field, String distance, Double latitude, Double longitude) {
        LatLonGeoLocation latLonGeoLocation = LatLonGeoLocation.of(
                builder -> builder.lat(latitude).lon(longitude)
        );
        GeoLocation geolocation = GeoLocation.of(
                builder -> builder.latlon(latLonGeoLocation)
        );
        GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery.of(
                builder -> builder.field(field)
                        .distance(distance)
                        .location(geolocation)
        );
        return Query.of(builder -> builder.geoDistance(geoDistanceQuery));
    }

    public static Query buildMultiMatchQuery(List<String> fields, String searchTerm) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                builder -> builder.query(searchTerm)
                        .fields(fields)
                        .fuzziness(Constants.Fuzzy.LEVEL)
                        .prefixLength(Constants.Fuzzy.PREFIX_LENGTH)
                        .type(TextQueryType.MostFields)
                        .operator(Operator.And)
        );
        return Query.of(builder -> builder.multiMatch(multiMatchQuery));
    }

    public static Aggregation buildTermsAggregation(String field) {
        TermsAggregation termsAggregation = TermsAggregation.of(
                builder -> builder.field(field).size(10)
        );
        return Aggregation.of(builder -> builder.terms(termsAggregation));
    }
}

//bool
//├── filter
//│   ├── range
//│   ├── geo_distance
//│   └── term
//│
//├── must
//│   └── multi_match
//│
//└── should
//    └── term
