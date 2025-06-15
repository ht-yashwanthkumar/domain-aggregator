package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Duplicate record filter
 */
@Component
public class DuplicateRecordFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateRecordFilter.class);

    private final Set<String> isVisited = ConcurrentHashMap.newKeySet();

    public boolean isDuplicate(NetworkRecord record) {
        String key = record.getTimestamp() + "|" + record.getSourceIp() + "|" +
                record.getDestinationIp() + "|" + record.getDomain();

        LOGGER.debug("Validating the key for duplicate entries {}", key);
        return !isVisited.add(key);
    }

    public void reset() {
        isVisited.clear();
    }
}
