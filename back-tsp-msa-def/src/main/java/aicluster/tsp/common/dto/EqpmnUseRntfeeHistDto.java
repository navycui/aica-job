package aicluster.tsp.common.dto;

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
@ApiModel(description = "사용료 부과내역")
public class EqpmnUseRntfeeHistDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8411222869079470721L;
	@ApiModelProperty(value = "구분")
	private String useDiv;
	@ApiModelProperty(value = "금액")
	private Integer rntfee;
	@ApiModelProperty(value = "사유")
	private String rqestResn;
	@ApiModelProperty(value = "일자")
	private Date creatDt;

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
}

