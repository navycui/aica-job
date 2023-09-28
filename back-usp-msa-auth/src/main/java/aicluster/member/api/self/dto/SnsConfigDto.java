package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsConfigDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 9093677756288878791L;
	private Boolean google;
	private Boolean naver;
	private Boolean kakao;
}
