package aicluster.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"aicluster", "bnet"})
public class AiPmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiPmsApplication.class, args);
	}

}
