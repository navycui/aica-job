package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptExpertExcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvlCmitExtrcExcelResultDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2842896088757454167L;

	String  mfcmmExtrcId;                   /** 위원추출ID */
	String  evlCmitId;                      /** 평가위원회ID */
	Integer extrcMultiple;                  /** 추출배수 */
	Integer odrNo;                          /** 차수번호(0부터시작. 0:기준차수) */

//	String[] aditExclCndCd;                 /** 추가배제조건코드(G:ADD_EXCL_CND) */
//	String[] partcptnLmttCndCd;             /** 참여제한조건코드(G:PARTCPTN_LMTT_CND) */
//	String[] expertClId;               		/** 전문가분류ID */

	/*결과용 추가 필드*/
	Integer totalOdr;                       /** 총차수 */
	String odrNm;                          	/** 차수명 */
	String extrcMultipleNm;                 /** 추출배수명 */
	String aditExclCndNm;                  	/** 추가배제조건명 */
	String partcptnLmttCndNm;              	/** 참여제한조건명 */
	String expertClNm;               		/** 전문가분류명 */

	List<UsptExpertExcel> extrcExpertExcelList; 			/** 추출전문가 리스트*/

	List<UsptExpertExcel> exclExpertExcelList;				/** 추출제외전문가 리스트*/
}
