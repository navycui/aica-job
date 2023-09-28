package aicluster.framework.common.util.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("nice-id")
public class NiceIdConfig {
	private String siteCode = null;
	private String sitePassword = null;
}
