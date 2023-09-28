package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5541203962284807356L;
	// 사업자유형
	private String memberType;
	// 사업자명
	private String memberNm;
	// 대표자명
	private String ceoNm;
	// 법인등록번호
	private String jurirno;
	// 담당자명
	private String chargerNm;
	// 사업자전환 SessionID
	private String sessionId;
}
