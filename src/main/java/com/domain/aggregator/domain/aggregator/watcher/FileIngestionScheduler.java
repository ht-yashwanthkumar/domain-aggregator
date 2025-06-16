package com.domain.aggregator.domain.aggregator.watcher;

import com.domain.aggregator.domain.aggregator.config.DomainAggregatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/*
 * File ingestion scheduler
 */
@Service
public class FileIngestionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileIngestionScheduler.class);

    private final JobLauncher jobLauncher;

    private final Job networkConnectionProcessingJob;

    private final DomainAggregatorConfiguration domainAggregatorConfiguration;

    // Tracks files already processed or processing
    private final Set<String> processedFiles = Collections.synchronizedSet(new HashSet<>());

    private final Map<String, Long> fileSizeMap = new HashMap<>();

    @Autowired
    public FileIngestionScheduler(JobLauncher jobLauncher, Job networkConnectionProcessingJob, DomainAggregatorConfiguration config) {
        this.jobLauncher = jobLauncher;
        this.networkConnectionProcessingJob = networkConnectionProcessingJob;
        this.domainAggregatorConfiguration = config;
    }

    @Scheduled(fixedDelayString = "${domain-aggregator-config.file-ingestion.file-scan-interval-ms}")
    public void pollDirectory() {
        try {
            File folder = new File(domainAggregatorConfiguration.getFileIngestion().getInputDirectory());
            if (!folder.exists() || !folder.isDirectory()) {
                LOGGER.error("Invalid input directory: {}", folder.getAbsolutePath());
                return;
            }

            File[] csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (csvFiles == null || csvFiles.length == 0) {
                LOGGER.info("No CSV files found in directory: {}", folder.getAbsolutePath());
                return;
            }

            for (File file : csvFiles) {
                try {
                    String filePath = file.getAbsolutePath();
                    if (processedFiles.contains(filePath)) {
                        continue; // file is already processed
                    }

                    if (isFileStable(file)) {
                        // Launch batch job for this file
                        JobParametersBuilder builder = new JobParametersBuilder();
                        builder.addString("filePath", filePath);
                        builder.addLong("timestamp", System.currentTimeMillis());
                        jobLauncher.run(networkConnectionProcessingJob, builder.toJobParameters());
                        processedFiles.add(filePath);
                    }
                } catch (Exception fileEx) {
                    LOGGER.error("Failed to process file {}: {}", file.getName(), fileEx.getMessage(), fileEx);
                }

            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error during file polling: {}", e.getMessage(), e);
        }
    }

    private boolean isFileReady(Path path) throws Exception {
        long size1 = Files.size(path);
        Thread.sleep(domainAggregatorConfiguration.getFileIngestion().getFileReadDelayMs());
        long size2 = Files.size(path);
        return size1 == size2;
    }

    private boolean isFileStable(File file) {
        String filePath = file.getAbsolutePath();
        long currentSize = file.length();

        if (!fileSizeMap.containsKey(filePath)) {
            fileSizeMap.put(filePath, currentSize);
            return false;
        }

        long previousSize = fileSizeMap.get(filePath);
        if (previousSize != currentSize) {
            fileSizeMap.put(filePath, currentSize);
            return false;
        }

        fileSizeMap.remove(filePath); // Cleanup once file is stable
        return true;
    }
}
