package com.domain.aggregator.domain.aggregator.reader;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import com.domain.aggregator.domain.aggregator.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Network record reader
 */
public class NetworkRecordReader implements ItemReader<NetworkRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkRecordReader.class);

    private final Resource inputResource;
    private Iterator<NetworkRecord> recordIterator;

    public NetworkRecordReader(Resource inputResource) {
        this.inputResource = inputResource;
    }

    @Override
    public NetworkRecord read() throws Exception {
        if (recordIterator == null) {
            this.recordIterator = extractRecordsFromInput().iterator();
        }
        return recordIterator.hasNext() ? recordIterator.next() : null;
    }

    private List<NetworkRecord> extractRecordsFromInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputResource.getInputStream()))) {
            String line = reader.readLine();

            if (!StringUtils.hasText(line)) return List.of();

            String cleanedLine = cleanHeader(line);
            String[] tokens = cleanedLine.split("[,\\s]+");

            return parseTokensToRecords(tokens);
        }
    }

    private String cleanHeader(String line) {
        if (line.startsWith(Constants.TIMESTAMP)) {
            int headerEnd = line.indexOf(Constants.DOMAIN) + Constants.DOMAIN.length();
            return line.substring(headerEnd).trim();
        }
        return line;
    }

    private List<NetworkRecord> parseTokensToRecords(String[] tokens) {
        List<NetworkRecord> records = new ArrayList<>();

        for (int i = 0; i + 5 < tokens.length; i += 6) {
            try {
                NetworkRecord record = NetworkRecord(tokens, i);
                records.add(record);
            } catch (Exception e) {
                LOGGER.error("Skipping due to invalid record at index {}. Error: {}", i, e.getMessage());
            }
        }

        return records;
    }

    private NetworkRecord NetworkRecord(String[] tokens, int index) {
        return new NetworkRecord(Instant.ofEpochMilli(Long.parseLong(tokens[index])).atZone(ZoneOffset.UTC).toLocalDateTime(), tokens[index + 1], tokens[index + 2], tokens[index + 3], tokens[index + 4], tokens[index + 5]);
    }
}