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
@ApiModel(description = "장비분류에 사용되는 Entity")
public class TsptEqpmnCl implements Serializable {
	/**
	 * 장비분류
	 */
	private static final long serialVersionUID = -5342192430475426445L;

	@ApiModelProperty(value = "장비분류 ID")
	private String eqpmnClId;				/** 장비분류ID */
	@ApiModelProperty(value = "부모장비분류 ID")
	private String eqpmnLclasId;		/** 부모장비분류ID */
	@ApiModelProperty(value = "정렬 순서")
	private Integer ordr;				/** 정렬순서 */
	@ApiModelProperty(value = "장비분류명")
	private String eqpmnClNm;				/** 장비분류명 */
	@ApiModelProperty(value = "사용유무")
	private boolean useAt;
	@ApiModelProperty(value = "생성자ID")
	private String creatrId;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "수정자ID")
	private String updusrId;
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;
	@ApiModelProperty(value = "depth")
	private Integer depth;

	public Date getCreatDt() {
        if (this.creatDt != null) {
            return new Date(this.creatDt.getTime());
        }
        return null;
    }
	public void setCreatDt(Date creatDt) {
        this.creatDt = null;
        if (creatDt != null) {
            this.creatDt = new Date(creatDt.getTime());
        }
    }
	public Date getUpdtDt() {
		if (this.updtDt != null) {
			return new Date(this.updtDt.getTime());
		}
		return null;
	}
	public void setUpdtDt(Date updtDt) {
		this.updtDt = null;
		if (updtDt != null) {
			this.updtDt = new Date(updtDt.getTime());
		}
	}
}
