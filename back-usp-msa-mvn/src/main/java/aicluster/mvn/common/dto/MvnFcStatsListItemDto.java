package aicluster.mvn.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcStatsListItemDto implements Serializable {

	private static final long serialVersionUID = -686663786149440369L;

	private String  mvnFcId			 ;      /** 입주시설ID     */
    private String  mvnId			 ;      /** 입주업체ID     */
    private String  cmpnyId			 ;      /** 업체ID         */
    private String  cmpnyNm			 ;      /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  bizrno			 ;      /** 사업자번호     */
    private String  jurirno			 ;      /** 법인등록번호   */
    @JsonIgnore
    private String  encMobileNo		 ;      /** 휴대폰번호 : CMMT_MEMBER.ENC_MOBILE_NO */
    @JsonIgnore
    private String  encEmail		 ;      /** 이메일 : CMMT_MEMBER.ENC_EMAIL */
    private String  mvnBeginDay		 ;      /** 입주일         */
    private String  mvnEndDay		 ;      /** 입주종료일     */
    private String  bnoCd			 ;      /** 건물동코드(G:BNO) */
    private String  bnoNm			 ;      /** 건물동명칭 : CMMT_CODE.CODE_NM */
    private String  roomNo			 ;      /** 건물방호수     */
    private long    mvnFcCapacity	;	    /** 수용인원       */
    private String  mvnFcar			 ;      /** 시설면적       */
    private String  mvnSt			 ;      /** 입주상태(G:MVN_ST) */
    private String  mvnStNm			 ;      /** 입주상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    public String getEmail() {
        if (string.isBlank(this.encEmail)) {
            return null;
        }
        // 이메일 복호화 및 마스킹
        return masking.maskingEmail(aes256.decrypt(this.encEmail, this.cmpnyId));
    }

    public String getMobileNo() {
        if (string.isBlank(this.encMobileNo)) {
            return null;
        }
        // 휴대폰번호 복호화 및 마스킹
        return masking.maskingMobileNo(aes256.decrypt(this.encMobileNo, this.cmpnyId));
    }

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
}
