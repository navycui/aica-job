package aicluster.tsp.api.admin.eqpmn.mngm.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "장비사용 현황")
public class MngmSearchParam {
  private static final long serialVersionUID = -2252201851279909084L;

  @ApiModelProperty(value = "장비ID")
  private String eqpmnId;
  @ApiModelProperty(value = "자산번호")
  private String assetsNo;
  @ApiModelProperty(value = "장비명(국문)")
  private String eqpmnNmKorean;
  @ApiModelProperty(value = "회원명/사업자명")
  private String userNm;
  @ApiModelProperty(value = "업체명")
  private String entrprsNm;
  @ApiModelProperty(value = "시작일")
  private Date useBeginDt;
  @ApiModelProperty(value = "종료일")
  private Date useEndDt;
  @ApiModelProperty(value = "장비분류명")
  private String eqpmnClNm;

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
