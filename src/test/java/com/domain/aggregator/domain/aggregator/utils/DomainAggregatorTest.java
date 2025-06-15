package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DomainAggregatorTest {

    @InjectMocks
    DomainAggregator domainAggregator;

    @Test
    void aggregate_shouldIncrementCountForSameDomain() {
        NetworkRecord record1 = mock(NetworkRecord.class);
        NetworkRecord record2 = mock(NetworkRecord.class);
        when(record1.getDomain()).thenReturn("example.com");
        when(record2.getDomain()).thenReturn("example.com");
        domainAggregator.aggregate(record1);
        domainAggregator.aggregate(record2);
        List<String> topDomains = domainAggregator.getTopDomains(1l);
        assertEquals(1, topDomains.size());
        assertTrue(topDomains.get(0).startsWith("example.com"));
        assertTrue(topDomains.get(0).contains("2 connections"));
    }

    @Test
    void aggregate_shouldHandleMultipleDomains() {
        NetworkRecord record1 = mock(NetworkRecord.class);
        NetworkRecord record2 = mock(NetworkRecord.class);
        NetworkRecord record3 = mock(NetworkRecord.class);
        when(record1.getDomain()).thenReturn("domain1.com");
        when(record2.getDomain()).thenReturn("domain2.com");
        when(record3.getDomain()).thenReturn("domain1.com");
        domainAggregator.aggregate(record1);
        domainAggregator.aggregate(record2);
        domainAggregator.aggregate(record3);
        List<String> topDomains = domainAggregator.getTopDomains(2l);
        assertEquals(2, topDomains.size());
        assertTrue(topDomains.get(0).startsWith("domain1.com")); // Should be first (2 connections)
        assertTrue(topDomains.get(1).startsWith("domain2.com")); // Should be second (1 connection)
    }

    @Test
    void getTopDomains_shouldLimitToTopN() {
        for (int i = 1; i <= 5; i++) {
            NetworkRecord record = mock(NetworkRecord.class);
            when(record.getDomain()).thenReturn("domain" + i + ".com");
            domainAggregator.aggregate(record);
        }

        List<String> top3 = domainAggregator.getTopDomains(3L);
        assertEquals(3, top3.size());
    }

    @Test
    void reset_shouldClearAllCounts() {
        NetworkRecord record = mock(NetworkRecord.class);
        when(record.getDomain()).thenReturn("test.com");
        domainAggregator.aggregate(record);
        assertEquals(1, domainAggregator.getTopDomains(1l).size());
        domainAggregator.reset();
        assertTrue(domainAggregator.getTopDomains(1l).isEmpty());
    }

    @Test
    void aggregate_shouldIgnoreNullRecords() {
        domainAggregator.aggregate(null);
        assertTrue(domainAggregator.getTopDomains(1l).isEmpty());
    }
}
