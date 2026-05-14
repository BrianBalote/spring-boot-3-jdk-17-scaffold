package org.balote.scaffold.container.extendable;

import lombok.extern.slf4j.Slf4j;
import org.balote.scaffold.container.shared.SybaseContainerSingleton;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Slf4j
public abstract class AbstractSybaseBaseContainerTest extends AbstractBaseContainerTest {

    @BeforeAll
    static void checkSybase() {
        SybaseContainerSingleton.startSybase();
    }

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:sybase:Tds:%s:%d/testdb",
                        SybaseContainerSingleton.getSybaseContainer().getHost(),
                        SybaseContainerSingleton.getSybaseContainer().getMappedPort(5000)));
    }
}
