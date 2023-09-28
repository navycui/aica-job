package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsptMvnFcltyInfo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 5767146611844490975L;

	private String  mvnFcId      ;                  /** 입주시설ID */
    private String  mvnFcNm      ;                  /** 입주시설명 */
    private String  mvnFcType    ;                  /** 입주시설유형(G:MVN_FC_TYPE) */
    private String  mvnFcTypeNm  ;                  /** 입주시설유형명 : CMMT_CODE.CODE_NM */
    private String  mvnFcDtype   ;                  /** 입주시설세부유형(G:MVN_FC_DTYPE) */
    private String  mvnFcDtypeNm ;                  /** 입주시설세부유형명 : CMMT_CODE.CODE_NM */
    private String  mvnFcCn      ;                  /** 입주시설내용 */
    private String  bnoCd        ;                  /** 건물동코드(G:BNO) : 건물동 공통코드 */
    private String  bnoNm        ;                  /** 건물동명 : CMMT_CODE.CODE_NM */
    private String  roomNo       ;                  /** 건물방호수 : 건물방호수 */
    private Long    mvnFcCapacity;                  /** 입주시설수용인원수 */
    private String  mvnFcar      ;                  /** 입주시설면적 */
    private String  reserveType  ;                  /** 예약유형(G:RESERVE_TYPE) */
    private String  reserveTypeNm;                  /** 예약유형명 : CMMT_CODE.CODE_NM */
    private Boolean hr24Yn       ;                  /** 24시간여부 */
    private String  utztnBeginHh ;                  /** 이용시작시 */
    private String  utztnEndHh   ;                  /** 이용종료시 */
    private String  mainFc       ;                  /** 주요시설 */
    private String  imageFileId  ;                  /** 이미지파일ID */
    private String  imageAltCn   ;                  /** 이미지ALT태그내용 */
    private Boolean enabled      ;                  /** 사용여부 */
    private String  mvnSt        ;                  /** 입주상태(G:MVN_ST) */
    private String  mvnStNm      ;                  /** 입주상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    mvnStDt      ;                  /** 입주상태변경일시 */
    private String  curOccupantId;                  /** 현재입주자ID(MEMBER_ID) */
    @JsonIgnore
    private String  creatorId    ;                  /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt    ;                  /** 생성일시 */
    @JsonIgnore
    private String  updaterId    ;                  /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt    ;                  /** 수정일시 */

    private Long rn;

    private List<UsptMvnFcltyRestde> usptMvnFcRsvtDdList ;

    public String getFmtMvnStDt() {
        if (this.mvnStDt == null) {
            return null;
        }
        return date.format(this.mvnStDt, "yyyy-MM-dd HH:mm:ss");
    }
}
