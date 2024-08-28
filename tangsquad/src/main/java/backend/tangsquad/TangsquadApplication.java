package backend.tangsquad;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class TangsquadApplication {
	public static void main(String[] args) {
		SpringApplication.run(TangsquadApplication.class, args);
	}

}
