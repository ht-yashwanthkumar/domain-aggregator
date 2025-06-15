package com.domain.aggregator.domain.aggregator;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;

import java.time.LocalDateTime;

public class MockData {
    public static NetworkRecord getNetworkRecord() {
        return new NetworkRecord(LocalDateTime.now(), "192.168.1.10", "5000", "10.0.0.", "443", "test.com");
    }
}
