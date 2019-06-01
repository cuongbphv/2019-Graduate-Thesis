package com.graduate.thesis.backend.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Value("${elastic.url}")
    private String elasticUrl;

    @Value("${elastic.host}")
    private String elasticHost;

    @Value("${elastic.port}")
    private int elasticPort;

    @Value("${elastic.protocol}")
    private String elasticProtocol;



    @Bean
    public JestClient createJestClient(){
        // Construct a new Jest client according to configuration via factory
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(elasticUrl)
                .multiThreaded(true)
                .build());

        return factory.getObject();
    }

    @Bean
    public RestClient createRestClient(){

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(elasticHost, elasticPort, elasticProtocol));
        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")};
        builder.setDefaultHeaders(defaultHeaders);

        return builder.build();
    }
}
