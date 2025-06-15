package com.domain.aggregator.domain.aggregator.reader;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
public class NetworkRecordReaderTest {

    @Mock
    Resource resource;

    @Test
    public void readNetworkRecord_Successfully() throws Exception {
        String content = "timestamp,src_ip,src_port,dst_ip,dst_port,domain1708867200000,192.168.1.10,5000,10.0.0.5,443,example.com 1708867205000,192.168.1.11,5001,10.0.0.6,80,test.com 1708867210000,192.168.1.12,5002,10.0.0.7,443,example.com";
        Mockito.when(resource.getInputStream()).thenReturn(getMockInputStream(content));
        NetworkRecordReader reader = new NetworkRecordReader(resource);
        NetworkRecord record1 = reader.read();
        Assertions.assertNotNull(record1);
        Assertions.assertEquals("192.168.1.10", record1.getSourceIp());
        Assertions.assertEquals("5000", record1.getSourcePort());
        Assertions.assertEquals("10.0.0.5", record1.getDestinationIp());
        Assertions.assertEquals("443", record1.getDestinationPort());
        Assertions.assertEquals("example.com", record1.getDomain());

        NetworkRecord record2 = reader.read();
        Assertions.assertNotNull(record2);
        Assertions.assertEquals("192.168.1.11", record2.getSourceIp());
        Assertions.assertEquals("5001", record2.getSourcePort());
        Assertions.assertEquals("10.0.0.6", record2.getDestinationIp());
        Assertions.assertEquals("80", record2.getDestinationPort());
        Assertions.assertEquals("test.com", record2.getDomain());

        NetworkRecord record3 = reader.read();
        Assertions.assertNotNull(record3);
        Assertions.assertEquals("192.168.1.12", record3.getSourceIp());
        Assertions.assertEquals("5002", record3.getSourcePort());
        Assertions.assertEquals("10.0.0.7", record3.getDestinationIp());
        Assertions.assertEquals("443", record3.getDestinationPort());
        Assertions.assertEquals("example.com", record3.getDomain());
    }

    @Test
    void readNetworkRecord_returnsNullWhenEmptyInput() throws Exception {
        Mockito.when(resource.getInputStream()).thenReturn(getMockInputStream(""));
        NetworkRecordReader reader = new NetworkRecordReader(resource);
        NetworkRecord record = reader.read();
        Assertions.assertNull(record);
    }

    private InputStream getMockInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
}
