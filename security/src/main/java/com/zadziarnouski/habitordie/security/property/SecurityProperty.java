package com.zadziarnouski.habitordie.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperty(String secretKey, Integer tokenExpirationHours) {

    public long getTokenExpirationDurationMs() {
        return tokenExpirationHours() * 60 * 60 * 1000L;
    }
}
