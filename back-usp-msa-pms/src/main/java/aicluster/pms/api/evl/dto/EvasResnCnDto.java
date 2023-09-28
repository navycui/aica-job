package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvasResnCnDto implements Serializable {
	private static final long serialVersionUID = -2309862033750332589L;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 회피사유내용 */
	private String evasResnCn;
}
