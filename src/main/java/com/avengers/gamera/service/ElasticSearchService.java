package com.avengers.gamera.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.avengers.gamera.config.ElasticSearchConfig;
import com.avengers.gamera.dto.article.EsArticleGetDto;
import com.avengers.gamera.entity.EsArticle;
import com.avengers.gamera.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {
    public static final String INDEX_NAME = "articles";
    private final ElasticSearchConfig elasticSearchConfig;
    private final ArticleMapper articleMapper;

    @SneakyThrows
    public List<EsArticleGetDto> searchArticle(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }

        ElasticsearchClient searchClient = elasticSearchConfig.getElasticsearchClient();
        SearchResponse<EsArticle> searchResponse = searchClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .query(keyword)
                                            .fields("title", "text", "game_name")
                                    ))
                            .postFilter(p -> p
                                    .bool(b -> b
                                            .must(m -> m
                                                    .term(t -> t
                                                            .field("is_deleted")
                                                            .value(false)
                                                    )))),
                    EsArticle.class);

        List<EsArticle> result = searchResponse.hits().hits().stream()
                .filter(h -> h.source() != null)
                .map(Hit::source)
                .toList();

        return result.stream().map(articleMapper::esArticleToEsArticleGetDto).toList();
    }
}
