package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlIem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -679689037333943148L;

	/** 순번 */
	private int rn;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 평가항목ID */
	String  evlIemId   ;
	/** 평가표ID */
	String  evlTableId ;
	/** 평가구분코드(G:EVL_DIV) */
	String  evlDivCd   ;
	/** 평가항목명 */
	String  evlIemNm   ;
	/** 평가항목내용 */
	String  evlIemCn   ;
	/** 배점점수 */
	Integer allotScore ;
	/** 평가기준번호 */
	Integer evlStdrNo  ;
	/** 정렬순서번호 */
	Integer sortOrdrNo ;

	/** 사용여부 */
	@JsonIgnore
	private Boolean enabled;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
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
