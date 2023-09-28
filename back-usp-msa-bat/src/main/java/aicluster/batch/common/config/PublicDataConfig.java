package aicluster.batch.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("publicdata")
public class PublicDataConfig {
	private String url;
	private String serviceKey;
}
