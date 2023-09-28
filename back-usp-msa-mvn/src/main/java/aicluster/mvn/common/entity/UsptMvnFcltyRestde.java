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
public class UsptMvnFcltyRestde implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 3457683030400725232L;

	private String  mvnFcId  ;                      /** 입주시설ID */
    private String  beginDay ;                      /** 시작일 */
    private String  endDay   ;                      /** 종료일 */
    private String  reason   ;                      /** 사유 */
    @JsonIgnore
    private String  creatorId;                      /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt;                      /** 생성일시 */
    @JsonIgnore
    private String  updaterId;                      /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt;                      /** 수정일시 */

    public String getFmtBeginDay() {
        if (this.beginDay == null || !date.isValidDate(this.beginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.beginDay), "yyyy-MM-dd");
    }

    public String getFmtEndDay() {
        if (this.endDay == null || !date.isValidDate(this.endDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.endDay), "yyyy-MM-dd");
    }
    
    public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
		}
		return null;
	}
	
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
	
	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
