package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsns implements Serializable {

	private static final long serialVersionUID = 5835611880639992817L;

	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
	/** 기준사업코드 */
	private String stdrBsnsCd;
	/** 사업년도구분코드 */
	private String bsnsYearTypeCd;
	/** 사업년도 */
	private String bsnsYear;
	/** 사업년도 상세코드 */
	private String bsnsYearDetailCd;
	/** 전담기관명 */
	private String ecshgInsttNm;
	/** 전담기관코드 */
	private String ecshgInsttCd;
	/** ERP코드 */
	private String erpCd;
	/** 총사업기간 */
	private int totBsnsPd;
	/** 컨소시엄여부 */
	private Boolean cnsrtm;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 과제유형코드 */
	private String taskTypeCd;
	/** 사용여부 */
	private Boolean enabled;
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
