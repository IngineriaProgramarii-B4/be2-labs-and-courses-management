package com.example.catalog.configurations;

import com.example.catalog.repositories.GradeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// <-------------------------------- FROM CATALOG ----------------------------------> //

@Configuration
public class GradeConfig {

    @Bean
    CommandLineRunner commanderLineRunner(GradeRepository repository){
        return args -> {
        };
    }
}
