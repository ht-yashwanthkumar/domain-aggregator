package com.domain.aggregator.domain.aggregator.writer;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import com.domain.aggregator.domain.aggregator.utils.DomainAggregator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NetworkRecordWriterTest {

    @Mock
    DomainAggregator domainAggregator;

    @InjectMocks
    NetworkRecordWriter networkRecordWriter;

    @Test
    public void write_shouldAggregateEachRecordInChunk() throws Exception {
        NetworkRecord record1 = mock(NetworkRecord.class);
        NetworkRecord record2 = mock(NetworkRecord.class);
        Chunk<NetworkRecord> chunk = new Chunk<>(List.of(record1, record2));
        networkRecordWriter.write(chunk);
        verify(domainAggregator, times(1)).aggregate(record1);
        verify(domainAggregator, times(1)).aggregate(record2);
    }

    @Test
    void write_shouldHandleEmptyChunkGracefully() throws Exception {
        Chunk<NetworkRecord> emptyChunk = new Chunk<>(List.of());
        networkRecordWriter.write(emptyChunk);
        verify(domainAggregator, never()).aggregate(any());
    }
}
