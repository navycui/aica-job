package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsPblancDetail;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class FrontBsnsPblancDto implements Serializable {
	private static final long serialVersionUID = -4933898211904207131L;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 상시모집여부 */
	private Boolean ordtmRcrit;
	/** 공고일 */
	private String pblancDay;
	/** 접수차수 */
	@JsonIgnore
	private Integer rceptOdr;
	/** 사업시작일  */
	@JsonIgnore
	private String bsnsBgnde;
	/** 사업종료일  */
	@JsonIgnore
	private String bsnsEndde;
	/** 사업기간  */
	private String bsnsPd;
	/** 사업규모 */
	private Long bsnsScale;
	/** 선정규모 */
	private int slctnScale;
	/** 공고요약 */
	private String pblancSumry;
	/** 접수기간  */
	private String rceptPd;
	/** 접수마감시분 */
	private String rceptClosingHm;
	/** 공고상태코드(G:PBLANC_STTUS) */
	private String pblancSttusCd;
	/** 공고상태 */
	private String pblancSttus;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 썸네일이미지파일ID */
	private String thumbnailFileId;
	/** 마감남은일 */
	private Integer rmndrDay;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업분야 */
	private String recomendCl;
	/** 모집대상 */
	private String applyMberType;
	/** 담당자ID */
	@JsonIgnore
	private String memberId;
	/** 담당자명 */
	private String memberNm;
	/** 직급 */
	private String positionNm;
	/** 이메일 */
	@JsonIgnore
	private String encEmail;
	/** 전화번호 */
	@JsonIgnore
	private String encTelNo;
	/** 이전공고ID */
	private String prePblancId;
	/** 이전공고명 */
	private String prePblancNm;
	/** 다음공고ID */
	private String nextPblancId;
	/** 다음공고명 */
	private String nextPblancNm;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> fileList;
	/** 상세목록 */
	List<UsptBsnsPblancDetail> detailList;

	/** 이메일 */
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}
	/** 전화번호 */
	public String getTelNo() {
		if (string.isBlank(this.encTelNo)) {
			return null;
		}
		String mobileNo = aes256.decrypt(this.encTelNo, this.memberId);
		return mobileNo;
	}

}
