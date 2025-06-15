package com.domain.aggregator.domain.aggregator.utils;

import com.domain.aggregator.domain.aggregator.MockData;
import com.domain.aggregator.domain.aggregator.model.NetworkRecord;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NetworkRecordValidatorTest {

    @Mock
    Validator validator;

    @InjectMocks
    NetworkRecordValidator networkRecordValidator;

    @Test
    void isValid_shouldReturnTrueForValidRecord() {
        NetworkRecord validRecord = MockData.getNetworkRecord();
        when(validator.validate(validRecord)).thenReturn(Collections.emptySet());
        boolean result = networkRecordValidator.isValid(validRecord);
        Assertions.assertTrue(result, "Expected valid record to return true");
    }

    @Test
    void isValid_shouldReturnFalseForInvalidRecord() {
        NetworkRecord invalidRecord = MockData.getNetworkRecord();
        ConstraintViolation<NetworkRecord> violation = mock(ConstraintViolation.class);
        when(validator.validate(invalidRecord)).thenReturn(Set.of(violation));
        boolean result = networkRecordValidator.isValid(invalidRecord);
        assertFalse(result, "Expected invalid record to return false");
    }

    @Test
    void isValid_shouldReturnFalseForNullRecord() {
        boolean result = networkRecordValidator.isValid(null);
        assertFalse(result, "Expected null record to return false");
    }

}
