package com.searchengine.searchservice.util;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class QueryRules {

    private static final String BOOST_FIELD_FORMAT = "%s^%f";

    public static final QueryRule STATE_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.state()),
            srp -> ElasticsearchUtil.buildTermQuery(Constants.Business.STATE, srp.state(), 1.0f)
    );

    public static final QueryRule OFFERINGS_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.offerings()),
            srp -> ElasticsearchUtil.buildTermQuery(Constants.Business.OFFERINGS_RAW, srp.offerings(), 1.0f)
    );

    public static final QueryRule RATING_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.rating()),
            srp -> ElasticsearchUtil.buildRangeQuery(Constants.Business.RATING, builder -> builder.gte(srp.rating()))
    );

    public static final QueryRule DISTANCE_QUERY = QueryRule.of(
            srp -> Stream.of(srp.distance(), srp.longitude(), srp.latitude()).allMatch(Objects::nonNull),
            srp -> ElasticsearchUtil.buildGeoDistanceQuery(Constants.Business.LOCATION, srp.distance(), srp.latitude(), srp.longitude())
    );

    public static final QueryRule CATEGORY_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.query()),  // can also use Predicates.isTrue() if it is true always
            srp -> ElasticsearchUtil.buildTermQuery(Constants.Business.CATEGORY_RAW, srp.query(), 5.0f)
    );

    private static final List<String> SEARCH_BOOST_FIELDS = List.of(
            boostField(Constants.Business.NAME, 2.0f),
            boostField(Constants.Business.CATEGORY, 1.5f),
            boostField(Constants.Business.OFFERINGS, 1.5f),
            boostField(Constants.Business.ADDRESS, 1.2f),
            Constants.Business.DESCRIPTION
    );

    private static String boostField(String field, float boost){
        return BOOST_FIELD_FORMAT.formatted(field, boost);
    }

    public static final QueryRule SEARCH_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.query()),  // can also use Predicates.isTrue() if it is true always
            srp -> ElasticsearchUtil.buildMultiMatchQuery(SEARCH_BOOST_FIELDS, srp.query())
    );
}
