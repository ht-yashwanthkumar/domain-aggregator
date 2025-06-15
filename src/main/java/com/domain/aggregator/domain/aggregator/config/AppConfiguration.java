package com.domain.aggregator.domain.aggregator.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * App configuration
 */
@Configuration
public class AppConfiguration {

    @Bean
    public Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
