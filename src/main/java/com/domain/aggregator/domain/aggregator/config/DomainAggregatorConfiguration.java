package com.domain.aggregator.domain.aggregator.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/*
 * Directory aggregator configuration
 */
@Configuration
@ConfigurationProperties("domain-aggregator-config")
@Validated
public class DomainAggregatorConfiguration {

    @NestedConfigurationProperty
    @Valid
    private DomainAggregatorConfiguration.FileIngestion fileIngestion;
    @NestedConfigurationProperty
    @Valid
    private DomainAggregatorConfiguration.DomainAnalytics domainAnalytics;

    public FileIngestion getFileIngestion() {
        return fileIngestion;
    }

    public void setFileIngestion(FileIngestion fileIngestion) {
        this.fileIngestion = fileIngestion;
    }

    public DomainAnalytics getDomainAnalytics() {
        return domainAnalytics;
    }

    public void setDomainAnalytics(DomainAnalytics domainAnalytics) {
        this.domainAnalytics = domainAnalytics;
    }

    @Validated
    public static class FileIngestion {
        @NotEmpty(message = "inputDirectory must not be empty")
        private String inputDirectory;

        @NotEmpty(message = "outputFilePath must not be empty")
        private String outputFilePath;

        @NotNull(message = "fileReadDelayMs must not be null")
        @Min(value = 5000, message = "fileReadDelayMs must be at least 5000 ms")
        private Long fileReadDelayMs;

        @NotNull(message = "fileScanIntervalMs must not be null")
        @Min(value = 10000, message = "fileScanIntervalMs must be at least 10000 ms")
        private Long fileScanIntervalMs;

        public String getInputDirectory() {
            return inputDirectory;
        }

        public void setInputDirectory(String inputDirectory) {
            this.inputDirectory = inputDirectory;
        }

        public String getOutputFilePath() {
            return outputFilePath;
        }

        public void setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
        }

        public Long getFileReadDelayMs() {
            return fileReadDelayMs;
        }

        public void setFileReadDelayMs(Long fileReadDelayMs) {
            this.fileReadDelayMs = fileReadDelayMs;
        }

        public Long getFileScanIntervalMs() {
            return fileScanIntervalMs;
        }

        public void setFileScanIntervalMs(Long fileScanIntervalMs) {
            this.fileScanIntervalMs = fileScanIntervalMs;
        }
    }

    @Validated
    public static class DomainAnalytics {
        @NotNull(message = "aggregationIntervalMs must not be null")
        @Min(value = 1000, message = "aggregationIntervalMs must be at least 1000 ms")
        private Long aggregationIntervalMs;

        @NotNull(message = "topDomainLimit must not be null")
        @Min(value = 1, message = "topDomainLimit must be greater than 0")
        private Long topDomainLimit;

        @NotNull(message = "template cannot be null")
        private String template;

        public Long getAggregationIntervalMs() {
            return aggregationIntervalMs;
        }

        public void setAggregationIntervalMs(Long aggregationIntervalMs) {
            this.aggregationIntervalMs = aggregationIntervalMs;
        }

        public Long getTopDomainLimit() {
            return topDomainLimit;
        }

        public void setTopDomainLimit(Long topDomainLimit) {
            this.topDomainLimit = topDomainLimit;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }
    }
}
