package aicluster.tsp.api.front.anals.param;

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
@ApiModel(description = "분석도구 신청")
public class FrontAnalsReqstParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "견전신청Id", hidden = true)
    private String reqstId;
    @ApiModelProperty(value = "신청자Id", hidden = true)
    private String creatrId;
    @ApiModelProperty(value = "분석도구 사용상태", hidden = true)
    private String useSttus;
    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "분석도구 타입")
    private String analsUntDiv;
    @ApiModelProperty(value = "자원Id")
    private String resrceId;
    @ApiModelProperty(value = "신청 사유")
    private String reqstResn;
    @ApiModelProperty(value = "템플릿타입")
    private String tmplatTy;
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
