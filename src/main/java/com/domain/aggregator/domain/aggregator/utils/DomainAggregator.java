package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Domain aggregator
 */
@Component
public class DomainAggregator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainAggregator.class);

    private final Map<String, AtomicInteger> domainCountsMap = new ConcurrentHashMap<>();

    public void aggregate(NetworkRecord record) {
        if (record == null) return;
        domainCountsMap.computeIfAbsent(record.getDomain(), k -> new AtomicInteger(0)).incrementAndGet();
    }

    public List<String> getTopDomains(Long topN) {
        LOGGER.info("Returning top {} domains", topN);
        return domainCountsMap.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get()))
                .limit(topN)
                .map(entry -> String.format("%s - %d connections", entry.getKey(), entry.getValue().get()))
                .toList();
    }

    public void reset() {
        LOGGER.info("Resetting domain counts... ");
        domainCountsMap.clear();
    }
}
