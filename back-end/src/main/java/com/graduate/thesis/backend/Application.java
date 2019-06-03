package com.graduate.thesis.backend;

import com.graduate.thesis.backend.config.FileStorageProperties;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.model.response.ClassifiedAdvertisingPagingResponse;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.repository.aggregation.CategoryAggregation;
import com.graduate.thesis.backend.service.elastic.ClassifiedAdvertisingElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class Application implements CommandLineRunner {

    @Autowired
    CategoryAggregation categoryAggregation;

    @Autowired
    AddressAggregation addressAggregation;

    @Autowired
    ClassifiedAdvertisingElasticService classifiedAdvertisingElasticService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {

//        classifiedAdvertisingElasticService.delete("5ce441a8e3e36a1bf8b36c46");

//        ClassifiedAdvertisingPagingResponse ahi =
//                classifiedAdvertisingElasticService.fullTextSearch(
//                "",
//                "mio",
//                new ArrayList<AdsMetadata>(),
//                0,
//                10);
//
//        System.out.println(ahi);
    }
}
