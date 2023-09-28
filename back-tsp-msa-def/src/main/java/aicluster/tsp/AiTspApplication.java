package aicluster.tsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"aicluster", "bnet"})
public class AiTspApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiTspApplication.class, args);
	}

}
