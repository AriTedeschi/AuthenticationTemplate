package internal.management.accounts.config.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Bean
    public CommandLineRunner logDatasourceUrl() {
        return args -> {
            logger.info("JDBC URL: {}", datasourceUrl);
        };
    }
}
