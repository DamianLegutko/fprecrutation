package pl.damianlegutko.fprecrutation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfiguration.class)
public class FpRecrutationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FpRecrutationApplication.class, args);
    }
}
