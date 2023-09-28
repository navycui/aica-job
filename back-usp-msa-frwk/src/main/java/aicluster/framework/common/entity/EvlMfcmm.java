package aicluster.framework.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvlMfcmm implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1272007800258048500L;
	private String evlMfcmmId;
	private String evlCmitId;
	private String expertId;
	private String encPassword;
	private String useBgnde;
	private String useEndde;
	private String refreshToken;
	private String atendSttusCd;
	private String atendDt;
}
