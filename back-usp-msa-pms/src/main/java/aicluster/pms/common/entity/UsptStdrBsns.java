package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptStdrBsns implements Serializable {

	private static final long serialVersionUID = 1046534507995629537L;

	/** 기준사업코드 */
	private String stdrBsnsCd;
	/** 산업분류 상위ID */
	private String parentBsnsClId;
	/** 산업분류 ID */
	private String bsnsClId;
	/** 기준사업명 */
	private String stdrBsnsNm;
	/** 시작년도 */
	private String beginYear;
	/** 총사업기간 */
	private int totBsnsPd;
	/** 사업유형코드 */
	private String bsnsTypeCd;
	/** 사업유형명 */
	private String bsnsTypeNm;
	/** 컨소시엄여부 */
	private Boolean cnsrtm;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 사업개요 */
	private String bsnsSumry;
	/** 전담기관명 */
	private String ecshgInsttNm;
	/** 전담기관코드 */
	private String ecshgInsttCd;
	/** ERP코드 */
	private String erpCd;
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
