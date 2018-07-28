package pl.damianlegutko.fprecrutation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import pl.damianlegutko.fprecrutation.configuration.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
public class FpRecrutationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FpRecrutationApplication.class, args);
    }
}
