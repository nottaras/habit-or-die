package com.zadziarnouski.habitordie.security.property;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.security.Key;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    @NotNull private Key key;

    @NotEmpty private String issuer;

    @NotNull private SignatureAlgorithm algorithm;

    @DurationMin(seconds = 1) @NotNull private Duration expiresIn;

    public void setAlgorithm(String algorithm) {
        this.algorithm = SignatureAlgorithm.valueOf(algorithm);
    }

    public void setKey(String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
}
