package pl.damianlegutko.fprecrutation;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "pl.damianlegutko.fprecrutation")
@EntityScan(basePackages = "pl.damianlegutko.fprecrutation")
@ComponentScan(basePackages = "pl.damianlegutko.fprecrutation")
public interface AppConfiguration {
}
