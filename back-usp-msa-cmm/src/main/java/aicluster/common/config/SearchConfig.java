package aicluster.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("search")
public class SearchConfig {
    private int connectionTimeout;	    // 서버 응답 시간 한도 설정
    private int socketTimeout;			// 연결 후 Read 하는 동안 특정 시각동안 패킷이 없을 경우 Connection 종료
	private String protocol;
	private String domain;
	private String searchApiUrl;
	private String arkApiUrl;
	private String popwordApiUrl;
	private String recommendApiUrl;
	private String recommandDomain;
	private String recommandListApiUrl;
	private String recommandDtlApiUrl;

	public String getSearchUrl() {
		return this.protocol + "://" + this.domain;
	}

	public String getSearchApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.searchApiUrl;
	}

	public String getArkApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.arkApiUrl;
	}
	
	public String getPopwordApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.popwordApiUrl;
	}
	
	public String getRecommendApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.recommendApiUrl;
	}
	
	public String getRecommandListApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.recommandListApiUrl;
	}

	public String getRecommandDtlApiUrl() {
		return this.protocol + "://" + this.domain + "" + this.recommandDtlApiUrl;
	}
	
}
