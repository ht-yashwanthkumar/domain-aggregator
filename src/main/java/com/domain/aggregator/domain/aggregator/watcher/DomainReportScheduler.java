package com.domain.aggregator.domain.aggregator.watcher;

import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration;
import com.domain.aggregator.domain.aggregator.utils.DomainAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.domain.aggregator.domain.aggregator.utils.Constants.*;

/*
 * Top domain report scheduler
 */
@Service
public class DomainReportScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainReportScheduler.class);

    private final DomainAggregator domainAggregator;

    private final DomainAggregatorConfiguration domainAggregatorConfiguration;

    @Autowired
    public DomainReportScheduler(DomainAggregator domainAggregator, DomainAggregatorConfiguration domainAggregatorConfiguration) {
        this.domainAggregator = domainAggregator;
        this.domainAggregatorConfiguration = domainAggregatorConfiguration;
    }

    @Scheduled(fixedRateString = "${domain-aggregator-config.domain-analytics.aggregation-interval-ms}")
    public void generateDomainReport() {
        try {
            List<String> topDomains = domainAggregator.getTopDomains(domainAggregatorConfiguration.getDomainAnalytics().getTopDomainLimit());
            if (topDomains.isEmpty()) {
                LOGGER.info("No domains found. Hence skipping domain generation");
                return;
            }

            String header = String.format(domainAggregatorConfiguration.getDomainAnalytics().getTemplate(),
                    domainAggregatorConfiguration.getDomainAnalytics().getTopDomainLimit(),
                    LocalDateTime.now().format(FORMATTER));

            String domainEntries = IntStream.range(0, topDomains.size())
                    .mapToObj(index -> String.format("%d%s%s%s", index + 1, DOT, SPACE, topDomains.get(index)))
                    .collect(Collectors.joining(SPACE));

            String content = String.join(SPACE, header, domainEntries) + System.lineSeparator();

            Path outputFile = Path.of(domainAggregatorConfiguration.getFileIngestion().getOutputFilePath());
            Files.createDirectories(outputFile.getParent());
            Files.writeString(outputFile, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            domainAggregator.reset();
            LOGGER.info("Domain report generated successfully");
        } catch (IOException e) {
            LOGGER.error("Failed to generate domain report due to IO error: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while generating domain report: {}", e.getMessage(), e);
        }
    }
}
