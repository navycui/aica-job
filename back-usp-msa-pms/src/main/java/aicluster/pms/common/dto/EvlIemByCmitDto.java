package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlIemByCmitDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6195959652917773506L;

	String  evlIemId   ;                    /** 평가항목ID */
	String  evlTableId ;                    /** 평가표ID */
	String  evlDivCd   ;                    /** 평가구분코드(G:EVL_DIV) */
	String  evlIemNm   ;                    /** 평가항목명 */
	String  evlIemCn   ;                    /** 평가항목내용 */
	Integer allotScore ;                    /** 배점점수 */
	Integer evlStdrNo  ;                    /** 평가기준번호 */
	Integer sortOrdrNo ;                    /** 정렬순서번호 */

	@JsonIgnore
	private Boolean enabled;        /** 사용여부 */
	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자 ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */

	List<EvlResultGnrlzDto> evlResultGnrlzDtoList; /*위원별 평가결과(서술형- 종합결과용)*/

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
