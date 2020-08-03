package com.jira.report.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ReactiveServicesExchangesConfig {
    private int connectionTimeout = 10_000;

    private int readTimeout = 60_000;

}

