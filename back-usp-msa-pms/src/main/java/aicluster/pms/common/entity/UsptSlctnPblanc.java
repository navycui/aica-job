package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptSlctnPblanc implements Serializable {
	private static final long serialVersionUID = 4571106849906652L;
	/** 선정공고ID */
	private String slctnPblancId;
	/** 선정공고명 */
	private String slctnPblancNm;
	/** 선정공고번호 */
	private String slctnPblancNo;
	/** 선정공고일 */
	private String slctnPblancDay;
	/** 선정공고요약 */
	private String slctnPblancSumry;
	/** 게시여부 */
	private Boolean ntce;
	/** 사용여부 */
	private Boolean enabled;
	/** 공고ID */
	private String pblancId;
	/** 접수차수 */
	private Integer rceptOdr;
	/** 평가단계ID */
	private String evlStepId;
	/** 분과ID */
	private String sectId;
	/** 평가최종선정ID */
	private String evlLastSlctnId;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

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

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
