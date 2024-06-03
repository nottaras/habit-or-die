package com.zadziarnouski.habitordie.profile.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zadziarnouski.habitordie.profile.property.FileUploadProperties;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MultipartFileValidatorTest {

    @InjectMocks
    private MultipartFileValidator multipartFileValidator;

    @Mock
    private FileUploadProperties fileUploadProperties;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Test
    public void givenValidFile_whenIsValidCalled_thenReturnTrue() {
        // Given
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        when(fileUploadProperties.getMaxSize()).thenReturn(1024L);
        when(fileUploadProperties.getAllowedTypes()).thenReturn(List.of("image/jpeg"));

        // When & Then
        assertTrue(multipartFileValidator.isValid(file, constraintValidatorContext));
    }

    @Test
    public void givenEmptyFile_whenIsValidCalled_thenReturnFalse() {
        // Given
        var file = new MockMultipartFile("file", new byte[0]);
        mockConstraintViolationSetup();

        // When
        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void givenFileSizeExceedingLimit_whenIsValidCalled_thenReturnFalse() {
        // Given
        var file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[2048]);
        when(fileUploadProperties.getMaxSize()).thenReturn(1024L);
        mockConstraintViolationSetup();

        // When
        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void givenEmptyListOfAllowedTypes_whenIsValidCalled_thenReturnFalse() {
        // Given
        var file = new MockMultipartFile("file", "test.pdf", null, "test".getBytes());
        mockConstraintViolationSetup();

        // When
        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void given_whenIsValidCalled_thenReturnFalse() {
        // Given
        var file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        when(fileUploadProperties.getAllowedTypes()).thenReturn(List.of("image/jpeg"));
        when(fileUploadProperties.getMaxSize()).thenReturn(1024L);
        mockConstraintViolationSetup();

        // When
        boolean isValid = multipartFileValidator.isValid(file, constraintValidatorContext);

        // Then
        assertFalse(isValid);
    }

    private void mockConstraintViolationSetup() {
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any()))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }
}
