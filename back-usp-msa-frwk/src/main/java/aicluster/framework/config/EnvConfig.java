package aicluster.framework.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
@ConfigurationProperties("env")
public class EnvConfig {
	private String systemId;
	private Cookie cookie;
	private Cors cors;
	private Dir dir;

	@Data
	public static class Cors {
		private List<String> origins;
		private List<String> headers;
		private List<String> methods;
		private String pattern;
		
		public List<String> getOrigins() {
			List<String> origins = new ArrayList<>();
			if(this.origins != null) {
				origins.addAll(this.origins);
			}
			return origins;
		}
		
		public void setOrigins(List<String> origins) {
			this.origins = new ArrayList<>();
			if(origins != null) {
				this.origins.addAll(origins);
			}
		}
		
		public List<String> getHeaders() {
			List<String> headers = new ArrayList<>();
			if(this.headers != null) {
				headers.addAll(this.headers);
			}
			return headers;
		}
		
		public void setHeaders(List<String> headers) {
			this.headers = new ArrayList<>();
			if(headers != null) {
				this.headers.addAll(headers);
			}
		}
		
		public List<String> getMethods() {
			List<String> methods = new ArrayList<>();
			if(this.methods != null) {
				methods.addAll(this.methods);
			}
			return methods;
		}
		
		public void setMethods(List<String> methods) {
			this.methods = new ArrayList<>();
			if(methods != null) {
				this.methods.addAll(methods);
			}
		}
	}

	@Data
	public static class Cookie {
		private String domain;
		private Integer maxAge;
		private Boolean secure;
		private Boolean httpOnly;
		private String path;

		public Boolean getSecure() {
			if (secure == null) {
				secure = false;
			}
			return secure;
		}

		public Boolean getHttpOnly() {
			if (httpOnly == null) {
				httpOnly = false;
			}
			return httpOnly;
		}
	}

	@Data
	public static class Dir {
		private String coreLog;
		private String upload;

		public String getTempDir() {
			if (string.isBlank(this.getUpload())) {
				throw new InvalidationException("파일 업로드 디렉터리 설정이 없습니다.");
			}

			String tmpDirName = this.getUpload() + File.separator + "_tmp";
			File tmpDir = new File(tmpDirName);
			if (!CoreUtils.file.isDirectory(tmpDir)) {
				tmpDir.mkdirs();
			}

			return tmpDirName;
		}
	}

	@PostConstruct
	public void init() {
		log.info("======================================");
		log.info("system-id:" + systemId);
		log.info("env:" + this);
		log.info("======================================");
	}

}
