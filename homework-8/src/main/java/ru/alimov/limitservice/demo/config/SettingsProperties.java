package ru.alimov.limitservice.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "settings")
public class SettingsProperties {
    private final BigDecimal defaultMaxDailyUserLimit;

    @ConstructorBinding
    public SettingsProperties(BigDecimal defaultMaxDailyUserLimit) {
        this.defaultMaxDailyUserLimit = defaultMaxDailyUserLimit;
    }

    public BigDecimal getDefaultMaxDailyUserLimit() {
        return defaultMaxDailyUserLimit;
    }
}
