package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class ExcclcListDto implements Serializable {
	private static final long serialVersionUID = 4704990508678717751L;
	/** 사업협약ID */
	private String bsnsCnvnId;
	/** 접수번호 */
	private String receiptNo;
	/** 과제명 */
	private String taskNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 회원명 */
	private String memberNm;
	/** 사업자등록번호 */
	private String bizrno;
	/** 처리상태 */
	private String processSttus;
	/** 번호 */
	private Long rn;
	/** 협약시작일 */
	private String cnvnBgnde;
	/** 협약종료일 */
	private String cnvnEndde;
	/** 협약총금액 */
	private Long totalWct;
	/** 과제신청사업비ID */
	private String taskReqstWctId;
	/** 신청예산 사업연도 */
	private String reqstBsnsYear;

	/** 정산일시 */
	@JsonIgnore
	private Date createdDt;
	public String getExcclcDate() {
		if(this.createdDt == null) {
			return "";
		}
		return CoreUtils.date.format(this.createdDt, "yyyy-MM-dd");
	}

	public String getCnvnBgnDate() {
		return CoreUtils.date.format(string.toDate(cnvnBgnde), "yyyy-MM-dd");
	}

	public String getCnvnEndDate() {
		return CoreUtils.date.format(string.toDate(cnvnEndde), "yyyy-MM-dd");
	}

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
         }
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
}
