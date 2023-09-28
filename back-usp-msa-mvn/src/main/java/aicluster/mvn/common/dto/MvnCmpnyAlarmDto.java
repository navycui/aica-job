package aicluster.mvn.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnCmpnyAlarmDto {
    private String  cmpnyId		;	       /** 업체ID */
    private String  cmpnyNm		;	       /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    @JsonIgnore
    private String  encEmail	;	       /** 이메일 : CMMT_MEMBER.ENC_EMAIL */
    private String  bnoCd		;	       /** 건물동코드(G:BNO) */
    private String  bnoNm		;	       /** 건물동명칭 : CMMT_CODE.CODE_NM */
    private String  roomNo		;	       /** 건물방호수 */
    private String  mvnBeginDay	;	       /** 입주시작일 */
    private String  mvnEndDay	;	       /** 입주종료일 */

    public String getEmail() {
        if (string.isBlank(this.encEmail)) {
            return null;
        }
        return aes256.decrypt(this.encEmail, this.cmpnyId);
    }

    public String getFmtMvnBeginDay() {
        if (string.isBlank(this.mvnBeginDay) || !date.isValidDate(this.mvnBeginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnBeginDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEndDay() {
        if (string.isBlank(this.mvnEndDay) || !date.isValidDate(this.mvnEndDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnEndDay), "yyyy-MM-dd");
    }
}
