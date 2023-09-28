package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "장비 할인적용")
public class EqpmnUseDscntDto implements Serializable {

    private static final long serialVersionUID = 362172459881192176L;
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "시간당 사용료")
    private Integer rntfeeHour;
    @ApiModelProperty(value = "할인율")
    private Integer dscntRate;
    @ApiModelProperty(value = "사용 시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용 종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "반출 여부")
    private Boolean tkoutAt;

    public Date getUseBeginDt() {
		if (this.useBeginDt != null) {
			return new Date(this.useBeginDt.getTime());
		}
		return null;
	}
    public void setUseBeginDt(Date useBeginDt) {
		this.useBeginDt = null;
		if (useBeginDt != null) {
			this.useBeginDt = new Date(useBeginDt.getTime());
		}
	}
    public Date getUseEndDt() {
    if (this.useEndDt != null) {
      return new Date(this.useEndDt.getTime());
    }
    return null;
  }
    public void setUseEndDt(Date useEndDt) {
        this.useEndDt = null;
        if (useEndDt != null) {
            this.useEndDt = new Date(useEndDt.getTime());
        }
    }
}
