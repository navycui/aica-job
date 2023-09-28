package aicluster.member.api.login.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("google")
public class GoogleConfig {
	private String appKey;
}
