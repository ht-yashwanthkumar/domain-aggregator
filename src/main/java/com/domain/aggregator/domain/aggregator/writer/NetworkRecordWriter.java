package com.domain.aggregator.domain.aggregator.writer;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import com.domain.aggregator.domain.aggregator.utils.DomainAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * Network record writer
 */
@Component
public class NetworkRecordWriter implements ItemWriter<NetworkRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkRecordWriter.class);

    @Autowired
    private DomainAggregator aggregator;

    @Override
    public void write(Chunk<? extends NetworkRecord> chunk) throws Exception {
        LOGGER.debug("Aggregating the network record for domain reports");
        chunk.forEach(aggregator::aggregate);
    }
}