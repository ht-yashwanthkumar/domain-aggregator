package com.domain.aggregator.domain.aggregator.watcher;

import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration;
import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration.DomainAnalytics;
import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration.FileIngestion;
import com.domain.aggregator.domain.aggregator.utils.DomainAggregator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainReportSchedulerTest {

    @Mock
    private DomainAggregator domainAggregator;

    @Mock
    private DomainAggregatorConfiguration domainAggregatorConfiguration;

    @InjectMocks
    private DomainReportScheduler domainReportScheduler;

    @Mock
    private FileIngestion fileIngestion;

    @Mock
    private DomainAnalytics domainAnalytics;

    @Test
    void testGenerateDomainReport_NoDomainsFound() {
        when(domainAggregator.getTopDomains(anyLong())).thenReturn(List.of());
        when(domainAggregatorConfiguration.getDomainAnalytics()).thenReturn(domainAnalytics);
        domainReportScheduler.generateDomainReport();
        verify(domainAggregator, times(1)).getTopDomains(anyLong());
        verifyNoMoreInteractions(domainAggregator);
    }

    @Test
    void testGenerateDomainReport_Success() throws IOException {
        List<String> topDomains = List.of("example.com", "test.com");
        when(domainAggregatorConfiguration.getDomainAnalytics()).thenReturn(domainAnalytics);
        when(domainAggregator.getTopDomains(anyLong())).thenReturn(topDomains);
        when(domainAnalytics.getTopDomainLimit()).thenReturn(2l);
        when(domainAnalytics.getTemplate()).thenReturn("Top %d Domains as of %s:");
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestion);
        when(fileIngestion.getOutputFilePath()).thenReturn("output/domain_report.txt");
        Path outputPath = Path.of("output/domain_report.txt");
        Files.createDirectories(outputPath.getParent());
        Files.deleteIfExists(outputPath);
        domainReportScheduler.generateDomainReport();
        verify(domainAggregator, times(1)).getTopDomains(anyLong());
        verify(domainAggregator, times(1)).reset();
        verify(domainAggregatorConfiguration, times(1)).getFileIngestion();
        assert Files.exists(outputPath);
    }

    @Test
    void shouldHandleIOExceptionGracefully(@TempDir Path tempDir) throws IOException {
        List<String> domains = List.of("google.com");
        when(domainAggregatorConfiguration.getDomainAnalytics()).thenReturn(domainAnalytics);
        when(domainAggregator.getTopDomains(anyLong())).thenReturn(domains);
        when(domainAnalytics.getTemplate()).thenReturn("Top %d Domains as of %s:");
        when(domainAnalytics.getTopDomainLimit()).thenReturn(2l);
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestion);
        when(fileIngestion.getOutputFilePath()).thenReturn(tempDir.toString());
        domainReportScheduler.generateDomainReport();
        verify(domainAggregator, never()).reset();
    }
}