package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/*
 * Network record validator
 */
@Component
public class NetworkRecordValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkRecordValidator.class);

    private final Validator validator;

    @Autowired
    public NetworkRecordValidator(Validator validator) {
        this.validator = validator;
    }

    public boolean isValid(NetworkRecord record) {
        LOGGER.info("Validating network record entry");

        if (record == null) return false;
        Set<ConstraintViolation<NetworkRecord>> violations = validator.validate(record);
        return violations.isEmpty();
    }
}
