package com.domain.aggregator.domain.aggregator.processor;

import com.domain.aggregator.domain.aggregator.MockData;
import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import com.domain.aggregator.domain.aggregator.utils.DuplicateRecordFilter;
import com.domain.aggregator.domain.aggregator.utils.NetworkRecordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NetworkRecordProcessorTest {

    @InjectMocks
    NetworkRecordProcessor networkRecordProcessor;

    @Mock
    DuplicateRecordFilter duplicateRecordFilter;

    @Mock
    NetworkRecordValidator networkRecordValidator;

    @Test
    public void processNetworkRecord_Successfully() throws Exception {
        Mockito.when(networkRecordValidator.isValid(Mockito.any())).thenReturn(true);
        Mockito.when(duplicateRecordFilter.isDuplicate(Mockito.any())).thenReturn(false);
        NetworkRecord record = networkRecordProcessor.process(MockData.getNetworkRecord());
        Assertions.assertNotNull(record);
        Assertions.assertEquals("test.com", record.getDomain());
    }

    @Test
    public void processNetworkRecord_DuplicateRecord() throws Exception {
        Mockito.when(networkRecordValidator.isValid(Mockito.any())).thenReturn(true);
        Mockito.when(duplicateRecordFilter.isDuplicate(Mockito.any())).thenReturn(true);
        NetworkRecord record = networkRecordProcessor.process(MockData.getNetworkRecord());
        Assertions.assertNull(record);
    }

    @Test
    public void processNetworkRecord_InvalidRecord() throws Exception {
        Mockito.when(networkRecordValidator.isValid(Mockito.any())).thenReturn(false);
        NetworkRecord record = networkRecordProcessor.process(MockData.getNetworkRecord());
        Assertions.assertNull(record);
    }
}
