package aicluster.tsp.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "공통 코드 그룹")
public class CmmtCodeGroup implements Serializable {

	private static final long serialVersionUID = 4774594622501860869L;

	@ApiModelProperty(value = "코드그룹")
	private String codeGroup;
	@ApiModelProperty(value = "코드그룹명")
	private String codeGroupNm;
	@ApiModelProperty(value = "비고")
	private String remark;
	@ApiModelProperty(value = "사용여부")
	private boolean enabled;
	@ApiModelProperty(value = "생성자ID(CMMT_MEMBER.MEMBER_ID)")
	private String creatorId;
	@ApiModelProperty(value = "생성일시")
	private Date createdDt;
	@ApiModelProperty(value = "수정자ID(CMMT_MEMBER.MEMBER_ID)")
	private String updaterId;
	@ApiModelProperty(value = "수정일시")
	private Date updatedDt;
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
