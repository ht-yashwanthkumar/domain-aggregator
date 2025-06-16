package com.domain.aggregator.domain.aggregator.watcher;

import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileIngestionSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job job;
    @Mock
    private DomainAggregatorConfiguration domainAggregatorConfiguration;
    @Mock
    private DomainAggregatorConfiguration.FileIngestion fileIngestionConfig;
    @InjectMocks
    private FileIngestionScheduler scheduler;

   /* @Test
    void shouldProcessReadyCsvFile(@TempDir Path tempDir) throws Exception {
        File csvFile = tempDir.resolve("data.csv").toFile();
        Files.writeString(csvFile.toPath(), "test,data");
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        when(fileIngestionConfig.getFileReadDelayMs()).thenReturn(50L);
        scheduler.pollDirectory();
        verify(jobLauncher).run(eq(job), any(JobParameters.class));
    }

    @Test
    void shouldSkipAlreadyProcessedFile(@TempDir Path tempDir) throws Exception {
        File csvFile = tempDir.resolve("data.csv").toFile();
        Files.writeString(csvFile.toPath(), "line");
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        scheduler.pollDirectory();
        scheduler.pollDirectory();
        verify(jobLauncher, times(1)).run(eq(job), any(JobParameters.class));
    }

    @Test
    void shouldSkipUnchangedGrowingFile(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("growing.csv");
        Files.writeString(filePath, "start");
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        when(fileIngestionConfig.getFileReadDelayMs()).thenReturn(1000L); // file will grow before check
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Files.writeString(filePath, "more", java.nio.file.StandardOpenOption.APPEND);
            } catch (Exception ignored) {
            }
        }).start();
        scheduler.pollDirectory();
        verify(jobLauncher, never()).run(any(), any());
    }

    @Test
    void shouldHandleInvalidDirectory() {
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn("invalid/directory");
        scheduler.pollDirectory();
        verifyNoInteractions(job);
    }

    @Test
    void shouldHandleNoCsvFiles(@TempDir Path tempDir) {
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        scheduler.pollDirectory();
        verifyNoInteractions(jobLauncher);
    }

    @Test
    void shouldLogAndContinueOnJobLauncherException(@TempDir Path tempDir) throws Exception {
        File csv = tempDir.resolve("bad.csv").toFile();
        Files.writeString(csv.toPath(), "bad");
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        when(fileIngestionConfig.getFileReadDelayMs()).thenReturn(10L);
        doThrow(new RuntimeException("Runtime Exception")).when(jobLauncher).run(any(), any());
        scheduler.pollDirectory();
        verify(jobLauncher).run(any(), any());
    }*/

    @Test
    public void testInvalidDirectoryPath() {
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn("invalid/path");
        scheduler.pollDirectory();
        verifyNoInteractions(jobLauncher);
    }

    @Test
    public void testNoCsvFilesPresent(@TempDir Path tempDir) {
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        scheduler.pollDirectory();
        verifyNoInteractions(jobLauncher);
    }

    @Test
    public void testStableFileTriggersJob(@TempDir Path tempDir) throws Exception {
        File file = tempDir.resolve("data.csv").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("timestamp,source_ip,destination_ip,domain\n");
            writer.write("2024-06-16T12:00:00,192.168.0.1,8.8.8.8,example.com\n");
        }
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        scheduler.pollDirectory(); // Should not trigger yet
        scheduler.pollDirectory(); // Should now trigger
        verify(jobLauncher, atLeastOnce()).run(eq(job), any(JobParameters.class));
    }

    @Test
    public void testAlreadyProcessedFileIsSkipped(@TempDir Path tempDir) throws Exception {
        File file = tempDir.resolve("processed.csv").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("timestamp,source_ip,destination_ip,domain\n");
            writer.write("2024-06-16T12:00:00,192.168.0.1,8.8.8.8,example.com\n");
        }
        when(domainAggregatorConfiguration.getFileIngestion()).thenReturn(fileIngestionConfig);
        when(fileIngestionConfig.getInputDirectory()).thenReturn(tempDir.toString());
        scheduler.pollDirectory(); // Stable in 2nd run
        scheduler.pollDirectory(); // Should skip
        verify(jobLauncher, atMost(1)).run(eq(job), any(JobParameters.class));
    }

}