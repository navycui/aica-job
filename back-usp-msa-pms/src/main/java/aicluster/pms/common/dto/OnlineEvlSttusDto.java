package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlineEvlSttusDto implements Serializable {

	private static final long serialVersionUID = -3022080299187032382L;

	String  evlCmitId;                      /** 평가위원회ID */
	@JsonIgnore
	String  sectId;                         /** 분과ID */
	String  sectNm;                         /** 분과명 */
	@JsonIgnore
	String  evlStepId;                      /** 평가단계ID */
	String  evlStepNm;                      /** 평가단계명 */
	@JsonIgnore
	String  evlTableId;                     /** 평가표ID */
	String  evlCmitNm;                      /** 평가위원회명 */
	@JsonIgnore
	String  orgnzrId;                       /** 간사ID(CMMT_INSIDER.MEMBER_ID) */
	String  orgnzrNm;                       /** 간사명(CMMT_INSIDER.MEMBER_ID) */
	@JsonIgnore
	String  evlSttusCd;                     /** 평가상태코드(G:EVL_ST) */
	String  evlSttusNm;                     /** 평가상태명(G:EVL_ST) */
	Date    evlSttusDt;                     /** 평가상태일시 */
	Boolean online;                         /** 온라인여부 */
	String  attachmentGroupId;              /** 첨부파일그룹ID */

	String  bsnsYear;                        /** 사업년도 */
	String  pblancNm;                        /** 공고명 */
	String  ordtmRcritNm;                    /** 상시모집명 */
	String  evlTypeCd;          			 /** 평가유형코드(G:EVL_TYPE) */
	String  evlTypeNm;          			 /** 평가유형명(G:EVL_TYPE) */
	String  processSttusNm;					 /** 처리상태명 */
	String  rceptOdr;						 /** 접수차수 */

	String  evlMthdCd;						 /** 평가방식코드 */

	List<EvlResultByTargetListDto> evlResultByTargetListDto; /*평가현황리스트*/
}
