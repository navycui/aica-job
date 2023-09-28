package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
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
public class MvnEtReqDto implements Serializable {

	private static final long serialVersionUID = -8510320029004325486L;

	private String  mvnEtReqId		    ;   /** 입주연장신청ID */
    private String  mvnId			    ;   /** 입주ID */
    private Date    mvnEtReqDt		    ;   /** 입주연장신청일시 */
    private String  mvnEtEndDay		    ;   /** 입주연장종료일 */
    private String  mvnEtReqCn		    ;   /** 입주연장신청내용 */
    private String  mvnEtReqSt		    ;   /** 입주연장신청상태(G: MVN_ET_REQ_ST) */
    private String  mvnEtReqStNm		;   /** 입주연장신청상태명 : CMMT_CODE.CODE_NM */
    private Date    mvnEtReqStDt		;   /** 입주연장신청상태변경일시 */
    private String  attachmentGroupId   ;   /** 첨부파일그룹ID */
    private String  makeupReqCn		    ;   /** 보완요청내용 */
    private String  dlbrtDay		    ;   /** 심의일 */
    private String  dlbrtAprvEndDay		;   /** 심의승인종료일 */
    private String  dlbrtResultDtlCn	;   /** 심의결과상세내용 */
    private String  dlbrtAtchGroupId	;   /** 심의결과첨부파일그룹ID */

    private UsptMvnEntrpsInfo mvnCompany;	       /** USPT_MVN_COMPANY Entity               */
    private UsptMvnFcltyInfo      mvnFc	;	       /** USPT_MVN_FC Entity                    */
    private MvnUserDto     mvnUser;			   /** 입주자 정보 */

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

    public String getFmtDlbrtDay() {
    	if (this.dlbrtDay == null || !date.isValidDate(this.dlbrtDay, "yyyyMMdd")){
            return null;
        }
    	return date.format(string.toDate(this.dlbrtDay), "yyyy-MM-dd");
	}

    public String getFmtDlbrtAprvEndDay() {
    	if (this.dlbrtAprvEndDay == null || !date.isValidDate(this.dlbrtAprvEndDay, "yyyyMMdd")){
            return null;
        }
    	return date.format(string.toDate(this.dlbrtAprvEndDay), "yyyy-MM-dd");
	}
}
