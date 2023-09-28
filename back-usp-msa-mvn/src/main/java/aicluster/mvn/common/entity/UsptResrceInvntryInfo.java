package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsptResrceInvntryInfo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1018630781125083753L;

	private String  rsrcId        ;                /** 자원ID */
	private String  rsrcGroupCd   ;                /** 자원그룹코드(G:RSRC_GROUP) */
	private String  rsrcGroupNm   ;                /** 자원그룹명 : CMMT_CODE.CODE_NM */
	private String  rsrcTypeNm    ;                /** 자원유형명 */
	private String  rsrcTypeUnitCd;                /** 자원유형단위코드(G:RSRC_TYPE_UNIT) */
	private String  rsrcTypeUnitNm;                /** 자원유형단위명 : CMMT_CODE.CODE_NM */
	private Integer invtQy        ;                /** 재고수량 */
	private Integer dstbQy        ;                /** 배분수량 */
	private Integer totalQy       ;                /** 전체수량 */
	private Integer rsrcCalcQy    ;                /** 자원연산량 */
	@JsonIgnore
	private String  creatorId     ;                /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt     ;                /** 생성일시 */
	@JsonIgnore
	private String  updaterId     ;                /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt     ;                /** 수정일시 */
	@JsonIgnore
	private String  procTypeCd    ;                /** 처리유형코드(Null:유지, C:입력, M:수정, D:삭제) */

	public int getInvtQy() {
		if (this.invtQy == null) {
			return 0;
		}
		return (int)this.invtQy;
	}

	public int getDstbQy() {
		if (this.dstbQy == null) {
			return 0;
		}
		return (int)this.dstbQy;
	}

	public int getTotalQy() {
		if (this.totalQy == null) {
			return 0;
		}
		return (int)this.totalQy;
	}

}