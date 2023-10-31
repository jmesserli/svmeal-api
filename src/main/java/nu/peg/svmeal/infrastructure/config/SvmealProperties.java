package nu.peg.svmeal.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "svmeal")
public record SvmealProperties(String userAgentLink) {}
