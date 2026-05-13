package org.balote.scaffold;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest
public class SybaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
