package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmmtMenu implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5698280712332834062L;
	private String systemId;		/** 시스템ID */
	private String menuId;			/** 메뉴ID */
	private String menuNm;			/** 메뉴이름 */
	private String url;				/** URL */
	private Boolean newWindow;		/** 새창여부 */
	private String parentMenuId;	/** 부모메뉴 ID */
	private Long sortOrder;			/** 정렬순서 */
	private String remark;			/** 비고 */
	@JsonIgnore
	private String creatorId;		/** 생성자ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */

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
