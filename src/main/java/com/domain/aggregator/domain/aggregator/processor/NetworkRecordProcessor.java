package com.domain.aggregator.domain.aggregator.processor;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import com.domain.aggregator.domain.aggregator.utils.DuplicateRecordFilter;
import com.domain.aggregator.domain.aggregator.utils.NetworkRecordValidator;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * Network record processor
 */
@Component
public class NetworkRecordProcessor implements ItemProcessor<NetworkRecord, NetworkRecord>, StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkRecordProcessor.class);

    private final DuplicateRecordFilter duplicateRecordFilter;
    private final NetworkRecordValidator validator;

    @Autowired
    public NetworkRecordProcessor(DuplicateRecordFilter duplicateRecordFilter, NetworkRecordValidator validator) {
        this.duplicateRecordFilter = duplicateRecordFilter;
        this.validator = validator;
    }

    @Override
    public NetworkRecord process(@Nonnull NetworkRecord networkRecord) throws Exception {
        LOGGER.info("Validating network record {} ", networkRecord);
        return (!validator.isValid(networkRecord) || duplicateRecordFilter.isDuplicate(networkRecord)) ? null : networkRecord;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        duplicateRecordFilter.reset();
    }
}
