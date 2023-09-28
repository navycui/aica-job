package aicluster.tsp.api.front.usereqst.estmt.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "견적신청 사용기간 선택")
public class UseReqstEstmtUseDateParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @JsonIgnore
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @JsonIgnore
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "검색시작일")
    private Date beginDt;
    @ApiModelProperty(value = "검색종료일")
    private Date endDt;

    public Date getBeginDt() {
		if (this.beginDt != null) {
			return new Date(this.beginDt.getTime());
		}
		return null;
	}
	public void setBeginDt(Date beginDt) {
		this.beginDt = null;
		if (beginDt != null) {
			this.beginDt = new Date(beginDt.getTime());
		}
	}
	public Date getEndDt() {
		if (this.endDt != null) {
			return new Date(this.endDt.getTime());
		}
		return null;
	}
	public void setEndDt(Date endDt) {
		this.endDt = null;
		if (endDt != null) {
			this.endDt = new Date(endDt.getTime());
		}
	}

}
