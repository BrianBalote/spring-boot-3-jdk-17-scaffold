package org.balote.scaffold.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class SybaseConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
