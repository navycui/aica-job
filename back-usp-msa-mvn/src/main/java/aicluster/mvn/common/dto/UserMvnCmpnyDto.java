package aicluster.mvn.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMvnCmpnyDto implements Serializable {

	private static final long serialVersionUID = -8283869009652248263L;

	private String mvnId			;	/** 입주ID                        */
    private String bsnsCd			;	/** 사업코드(BS + 8자리 순번) */
	private String evlLastSlctnId	;	/** 평가최정선정ID : USPT_BSNS_SLCTN.EVL_LAST_SLCTN_ID */
	private String lastSlctnTrgetId	;   /** 최종선정대상ID : USPT_BSNS_SLCTN.LAST_SLCTN_TRGET_ID */
    private String bsnsSlctnId		;	/** 사업선정대상ID */
    private String applyId			;	/** 신청ID */
	private String cmpnyId			;	/** 업체ID                        */
	private String cmpnyNm			;	/** 업체명(CMMT_MEMBER.MEMBER_NM) */
	private String mvnBeginDay		;	/** 입주일                        */
	private String mvnEndDay		;	/** 입주종료일                    */
	private Long   mvnEtNum			;	/** 입주연장횟수                  */
	private String mvnFcId			;	/** 입주시설ID                    */
	private String mvnFcNm			;	/** 입주시설명                    */
	private String bnoCd			;	/** 건물동호수코드                */
	private String bnoNm			;	/** 건물동호수 명                 */
	private String roomNo			;	/** 건물방호수                    */
	private Long   mvnFcCapacity	;	/** 수용인원                      */
	private String mvnFcar			;	/** 시설면적                      */
	private String mvnSt			;	/** 입주상태                      */
	private String mvnStNm			;	/** 입주상태명                    */
	private String mvnCmpnySt		;	/** 입주업체상태                  */
	private String mvnCmpnyStNm		;	/** 입주업체상태명                */
	private String mvnAllocateSt	;	/** 입주배정상태                  */
	private String mvnAllocateStNm	;	/** 입주배정상태명                */
	private String recentPresentnYm ;	/** 입주업체성과 최근제출년월     */

    public String getFmtMvnBeginDay() {
        if (this.mvnBeginDay == null || !date.isValidDate(this.mvnBeginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnBeginDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEndDay() {
        if (this.mvnEndDay == null || !date.isValidDate(this.mvnEndDay, "yyyyMMdd")){
            return null;
        }
        return date.format(string.toDate(this.mvnEndDay), "yyyy-MM-dd");
    }
}
