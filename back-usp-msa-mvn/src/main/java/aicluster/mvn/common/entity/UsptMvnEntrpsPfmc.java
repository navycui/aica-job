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
public class UsptMvnEntrpsPfmc implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -4318296606625166678L;

	private String  mvnId    ;                      /** 입주ID */
    private String  sbmsnYm  ;                      /** 제출년월 */
    private String  rsltId   ;                      /** 성과ID */
    private String  sbmsnDay ;                      /** 제출일 */
    @JsonIgnore
    private String  creatorId;                      /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt;                      /** 생성일시 */
    @JsonIgnore
    private String  updaterId;                      /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt;                      /** 수정일시 */

    public String getFmtSbmsnYm() {
    	if (string.isBlank(this.sbmsnYm) || date.isValidDate(this.sbmsnYm, "yyyyMM")) {
    		return null;
    	}
    	return date.format(string.toDate(this.sbmsnYm), "yyyy-MM");
    }

    public String getFmtSbmsnDay() {
    	if (string.isBlank(this.sbmsnDay) || !date.isValidDate(this.sbmsnDay, "yyyyMMdd")) {
    		return null;
    	}
    	return date.format(string.toDate(this.sbmsnDay), "yyyy-MM-dd");
	}
}
