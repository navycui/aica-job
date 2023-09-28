package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfModifyParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7341643802022511727L;
	private String memberNm;			/** 회원명(사업자명): 모두 변경가능 */
	private String ceoNm;				/** 대표자명: 사업자일 경우에만 변경가능 */
	private String chargerNm;			/** 담당자명: 사업자일 경우에만 변경가능 */
	private Boolean marketingReception;	/** 마케팅 정보 수신 동의: 사업자일 경우에만 변경가능 */
}
