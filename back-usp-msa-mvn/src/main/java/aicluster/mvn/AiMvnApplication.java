package aicluster.mvn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"aicluster", "bnet"})
public class AiMvnApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiMvnApplication.class, args);
	}

}
