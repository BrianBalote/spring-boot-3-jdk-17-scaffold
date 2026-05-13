package org.balote.scaffold.config;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.cassandra")
public class CassandraConfig {

    private String contactPoints;
    private int port;
    private String keyspaceName;
    private String localDatacenter;
    private String username;
    private String password;

    @Bean
    public CqlSession cqlSession() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints, port))
                .withLocalDatacenter(localDatacenter)
                .withAuthCredentials(username, password)
                .build();
    }
}
