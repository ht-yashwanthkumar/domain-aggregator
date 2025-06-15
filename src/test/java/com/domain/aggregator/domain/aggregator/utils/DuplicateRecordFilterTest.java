package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.MockData;
import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DuplicateRecordFilterTest {

    @InjectMocks
    DuplicateRecordFilter duplicateRecordFilter;

    @Test
    void isDuplicate_shouldReturnFalseOnFirstOccurrence() {
        NetworkRecord record = MockData.getNetworkRecord();
        boolean result = duplicateRecordFilter.isDuplicate(record);
        assertFalse(result, "Expected first record to not be a duplicate");
    }

    @Test
    void isDuplicate_shouldReturnTrueOnDuplicateRecord() {
        NetworkRecord record = MockData.getNetworkRecord();
        duplicateRecordFilter.isDuplicate(record); // first call
        boolean secondCall = duplicateRecordFilter.isDuplicate(record); // duplicate check
        assertTrue(secondCall, "Expected duplicate record to return true");
    }

    @Test
    void isDuplicate_shouldHandleDifferentRecordsSeparately() {
        NetworkRecord record1 = MockData.getNetworkRecord();
        NetworkRecord record2 = MockData.getNetworkRecord();
        record2.setDomain("unique.com");
        duplicateRecordFilter.isDuplicate(record1);
        boolean result = duplicateRecordFilter.isDuplicate(record2);
        assertFalse(result, "Expected different record not to be considered duplicate");
    }

    @Test
    void reset_shouldClearDuplicateMemory() {
        NetworkRecord record = MockData.getNetworkRecord();
        duplicateRecordFilter.isDuplicate(record); // Add to set
        duplicateRecordFilter.reset();             // Clear set
        boolean result = duplicateRecordFilter.isDuplicate(record); // Should behave like first time
        assertFalse(result, "Expected false after reset for previously seen record");
    }
}
