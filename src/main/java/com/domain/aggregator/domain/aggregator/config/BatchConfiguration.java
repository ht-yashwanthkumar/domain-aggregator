package com.domain.aggregator.domain.aggregator.config;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
//import com.domain.aggregator.domain.aggregator.reader.NetworkRecordReader;
import com.domain.aggregator.domain.aggregator.reader.NetworkRecordReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Batch job configuration
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job networkConnectionProcessingJob(@Qualifier("networkConnectionStep") Step step) {
        return new JobBuilder("networkConnectionProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean(name = "networkConnectionStep")
    public Step processNetworkConnectionStep(ItemReader<NetworkRecord> reader,
                                             ItemProcessor<NetworkRecord, NetworkRecord> processor, ItemWriter<NetworkRecord> writer) {
        return new StepBuilder("processNetworkConnectionStep", jobRepository)
                .<NetworkRecord, NetworkRecord>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<NetworkRecord> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new NetworkRecordReader(new FileSystemResource(filePath));
    }
}
