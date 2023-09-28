package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptMvnEtReqst implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 5649550249799466550L;

	private String  mvnEtReqId       ;              /** 입주연장신청ID */
    private String  mvnId            ;              /** 입주ID */
    @JsonIgnore
    private Date    mvnEtReqDt       ;              /** 입주연장신청일시 */
    private String  mvnEtEndDay      ;              /** 입주연장종료일 */
    private String  mvnEtReqCn       ;              /** 입주연장신청내용 */
    private String  mvnEtReqSt       ;              /** 입주연장신청상태(G:MVN_ET_REQ_ST) */
    private String  mvnEtReqStNm     ;              /** 입주연장신청상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    mvnEtReqStDt     ;              /** 입주연장신청상태일시 */
    private String  attachmentGroupId;              /** 첨부파일그룹ID */
    private String  makeupReqCn      ;              /** 보완요청내용 */
    private String  dlbrtDay         ;              /** 심의일 */
    private String  dlbrtAprvEndDay  ;              /** 심의승인종료일 */
    private String  dlbrtResultDtlCn ;              /** 심의결과상세내용 */
    private String  dlbrtAtchGroupId ;              /** 심의결과첨부파일그룹ID */
    @JsonIgnore
    private String  creatorId        ;              /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt        ;              /** 생성일시 */
    @JsonIgnore
    private String  updaterId        ;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt        ;              /** 수정일시 */

    public String getFmtMvnEtReqDt() {
        if (this.mvnEtReqDt == null) {
            return null;
        }
        return date.format(this.mvnEtReqDt, "yyyy-MM-dd");
    }

    public String getFmtMvnEtEndDay() {
        if (this.mvnEtEndDay == null || !date.isValidDate(this.mvnEtEndDay, "yyyyMMdd")){
            return null;
        }
        return date.format(string.toDate(this.mvnEtEndDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEtReqStDt() {
        if (this.mvnEtReqStDt == null) {
            return null;
        }
        return date.format(this.mvnEtReqStDt, "yyyy-MM-dd HH:mm:ss");
    }
}
