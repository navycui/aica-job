package aicluster.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("email")
public class EmailConfig {
	private String url;
	private String appKey;
	private String secretKey;
	private String senderEmail;
	private String senderName;
}
