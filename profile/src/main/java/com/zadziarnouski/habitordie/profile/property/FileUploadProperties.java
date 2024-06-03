package com.zadziarnouski.habitordie.profile.property;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.file.upload")
public class FileUploadProperties {

    @NotNull private Long maxSize;

    @NotEmpty private List<String> allowedTypes;
}
