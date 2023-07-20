package com.shop.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing(auditorAwareRef = "testAuditorAware")
@Configuration
public class TestJpaConfig {
}
