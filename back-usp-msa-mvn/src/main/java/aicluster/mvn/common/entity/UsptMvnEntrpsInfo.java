package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsptMvnEntrpsInfo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 6569706153961495574L;

	private String  mvnId          ;                /** 입주ID */
    private String  bsnsCd         ;                /** 사업코드(BS + 8자리 순번) */
	private String  evlLastSlctnId ;				/** 평가최정선정ID : USPT_BSNS_SLCTN.EVL_LAST_SLCTN_ID */
	private String  lastSlctnTrgetId;           	/** 최종선정대상ID : USPT_BSNS_SLCTN.LAST_SLCTN_TRGET_ID */
    private String  bsnsSlctnId    ;                /** 사업선정대상ID */
    private String  mvnFcId        ;                /** 입주시설ID */
    private String  mvnBeginDay    ;                /** 입주시작일 */
    private String  mvnEndDay      ;                /** 입주종료일 */
    private String  cmpnyId        ;                /** 업체ID : CMMT_MEMBER.MEMBER_ID */
    private String  cmpnyNm        ;                /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  ceoNm          ;                /** 대표자명 : CMMT_MEMBER.CEO_NM */
    private String  bizrno         ;                /** 사업자번호 : CMMT_MEMBER.BIZRNO */
    private String  jurirno        ;                /** 법인등록번호 : CMMT_MEMBER.JURIRNO */
    private String  equipPvdtl     ;                /** 장비제공내역 */
    private int     mvnEtNum       ;                /** 입주연장횟수 */
    private String  mvnCmpnySt     ;                /** 입주업체상태(G:MVN_CMPNY_ST) */
    private String  mvnCmpnyStNm   ;                /** 입주업체상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    mvnCmpnyStDt   ;                /** 입주업체상태변경일시 */
    private String  mvnAllocateSt  ;                /** 입주배정상태(G:MVN_ALLOCATE_ST) */
    private String  mvnAllocateStNm;                /** 입주배정상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    mvnAllocateStDt;                /** 입주배정상태변경일시 */
    @JsonIgnore
    private String  creatorId      ;                /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt      ;                /** 생성일시 */
    @JsonIgnore
    private String  updaterId      ;                /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt      ;                /** 수정일시 */
    private String  bnoCd          ;                /** 건물동코드(G:BNO) : 건물동 공통코드 */
    private String  bnoNm          ;                /** 건물동명 : CMMT_CODE.CODE_NM */
    private String  roomNo         ;                /** 건물방호수 : 건물방호수 */
    private String  applyId        ;                /** 신청ID */

    public String getFmtMvnBeginDay() {
        if (this.mvnBeginDay == null || !date.isValidDate(this.mvnBeginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnBeginDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEndDay() {
        if (this.mvnEndDay == null || !date.isValidDate(this.mvnEndDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnEndDay), "yyyy-MM-dd");
    }

    public String getFmtMvnCmpnyStDt() {
        if (this.mvnCmpnyStDt == null) {
            return null;
        }
        return date.format(this.mvnCmpnyStDt, "yyyy-MM-dd HH:mm:ss");
    }

    public String getFmtMvnAllocateStDt() {
        if (this.mvnAllocateStDt == null) {
            return null;
        }
        return date.format(this.mvnAllocateStDt, "yyyy-MM-dd HH:mm:ss");
    }
}
