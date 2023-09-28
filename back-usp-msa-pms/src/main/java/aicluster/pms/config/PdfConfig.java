package aicluster.pms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("pdf")
public class PdfConfig {
	/** 서버 구분
	 * (AICA, DEV)
	 */
	private String server;
	/** 서버 URI */
	private String uri;
	/** gateway 서버 input 폴더 */
	private String inputFolder;
	/** gateway 서버 output 폴더 */
	private String outputFolder;
	/** PMS 도메인 */
	private String pmsDomain;
	/** pms input 파일 폴더 */
	private String pmsInputFolder;
}
