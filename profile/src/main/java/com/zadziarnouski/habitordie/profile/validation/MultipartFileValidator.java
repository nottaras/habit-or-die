package com.zadziarnouski.habitordie.profile.validation;

import com.zadziarnouski.habitordie.profile.property.FileUploadProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MultipartFileValidator implements ConstraintValidator<ValidMultipartFile, MultipartFile> {

    private final FileUploadProperties fileUploadProperties;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (isFileEmptyOrNull(file)) {
            addConstraintViolation(context, "File is empty or null");
            return false;
        }

        if (isFileSizeExceedingLimit(file)) {
            addConstraintViolation(context, "File size exceeds the maximum limit");
            return false;
        }

        if (isInvalidFileType(file)) {
            addConstraintViolation(context, "Invalid file type");
            return false;
        }

        return true;
    }

    private boolean isFileEmptyOrNull(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private boolean isFileSizeExceedingLimit(MultipartFile file) {
        return file.getSize() > fileUploadProperties.getMaxSize();
    }

    private boolean isInvalidFileType(MultipartFile file) {
        return !fileUploadProperties.getAllowedTypes().isEmpty()
                && !fileUploadProperties.getAllowedTypes().contains(file.getContentType());
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
