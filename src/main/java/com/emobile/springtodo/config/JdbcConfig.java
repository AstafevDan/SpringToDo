package com.emobile.springtodo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

/**
 * Класс с конфигурацией JDBC.
 */
@Configuration
@EnableJdbcAuditing
public class JdbcConfig {
}
