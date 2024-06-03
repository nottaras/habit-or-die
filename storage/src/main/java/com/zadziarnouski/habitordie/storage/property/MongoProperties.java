package com.zadziarnouski.habitordie.storage.property;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.data.mongodb")
public record MongoProperties(@NotEmpty String database) {}
