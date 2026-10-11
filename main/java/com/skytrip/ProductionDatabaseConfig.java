package com.skytrip;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionDatabaseConfig {
    @Bean
    DataSource dataSource() {
        String raw = System.getenv("DATABASE_URL");
        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException("DATABASE_URL não configurada para produção.");
        }
        try {
            URI uri = URI.create(raw.replace("postgres://", "postgresql://"));
            String[] userInfo = uri.getUserInfo().split(":", 2);
            String username = URLDecoder.decode(userInfo[0], StandardCharsets.UTF_8);
            String password = userInfo.length > 1 ? URLDecoder.decode(userInfo[1], StandardCharsets.UTF_8) : "";
            String path = uri.getPath();
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + (uri.getPort() > 0 ? uri.getPort() : 5432) + path;
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) jdbcUrl += "?" + uri.getQuery();
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(jdbcUrl);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setMaximumPoolSize(5);
            return ds;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível interpretar DATABASE_URL do banco PostgreSQL.", e);
        }
    }
}
