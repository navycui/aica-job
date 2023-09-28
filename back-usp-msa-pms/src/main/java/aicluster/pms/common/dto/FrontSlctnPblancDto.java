package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptSlctnPblancDetail;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class FrontSlctnPblancDto implements Serializable {
	private static final long serialVersionUID = 7194426205334640808L;
	/** 선정공고ID */
	private String slctnPblancId;
	/** 공고명 */
	private String slctnPblancNm;
	/** 선정공고요약 */
	private String slctnPblancSumry;
	/** 선정공고일 */
	private String slctnPblancDay;
	/** 선정공고번호 */
	private String slctnPblancNo;
	/** 공고명 */
	private String pblancNm;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 담당자ID */
	@JsonIgnore
	private String memberId;
	/** 담당자명 */
	private String memberNm;
	/** 직급 */
	private String positionNm;
	/** 첨부파일그룹ID */
	@JsonIgnore
	private String attachmentGroupId;
	/** 이메일 */
	@JsonIgnore
	private String encEmail;
	/** 전화번호 */
	@JsonIgnore
	private String encTelNo;
	/** 이전선정공고ID */
	private String preSlctnPblancId;
	/** 이전선정공고명 */
	private String preSlctnPblancNm;
	/** 다음선정공고ID */
	private String nextSlctnPblancId;
	/** 다음선정공고명 */
	private String nextSlctnPblancNm;
	/** 상세목록 */
	List<UsptSlctnPblancDetail> detailList;
	/** 선정업체 목록 */
	List<FrontSlctnPblancSlctnDto> slctnList;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> fileList;

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
