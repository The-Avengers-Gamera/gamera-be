package com.avengers.gamera.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
@Slf4j
public class ElasticSearchConfig {
    private Boolean enabled;
    private String host;
    private int port;
    private String username;
    private String password;

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        if (!enabled) {
            log.info("Elasticsearch is disabled");
            return null;
        }

        RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();

        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();
        jacksonJsonpMapper.objectMapper().registerModule(new JavaTimeModule());

        ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);

        return new ElasticsearchClient(transport);
    }
}
