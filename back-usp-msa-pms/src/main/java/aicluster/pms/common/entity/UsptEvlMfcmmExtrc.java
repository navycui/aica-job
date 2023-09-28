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
public class UsptEvlMfcmmExtrc implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3331789917369019466L;

	/** 위원추출ID */
	String  mfcmmExtrcId;
	/** 평가위원회ID */
	String  evlCmitId;
	/** 차수번호(0부터시작. 0:기준차수) */
	Integer odrNo;
	/** 추출배수 */
	Integer extrcMultiple;
	/** 추가배제조건코드(G:ADD_EXCL_CND) */
	String[] aditExclCndCd;
	/** 참여제한조건코드(G:PARTCPTN_LMTT_CND) */
	String[] partcptnLmttCndCd;
	/** 전문가분류ID */
	String[] expertClId;

	//이해관계자 전문가ID
	String[] intprtsExpertId;

	//주관기관관련전문가 ID
	String[] insttExpertId;

	//추가배제조건1
	String exclCnd1;

	//추가배제조건2
	String exclCnd2;

	//추가배제조건3
	String exclCnd3;

	//추가배제조건4
	String exclCnd4;

	//참여제한조건1
	String lmttCndCd1;

	//참여제한조건2
	String lmttCndCd2;

	//참여제한조건3
	String lmttCndCd3;

	/** 섭외결과상태코드 */
	String  lsnResultCd;

	/** 엑셀다운 작업내용 */
	String excelDwldWorkCn;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

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
