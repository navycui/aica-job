package aicluster.tsp.api.front.mypage.resrce.param;

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
@ApiModel(description = "실증자원 목록리스트")
public class MyResrceListParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;

    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "GPU사용여부")
    private boolean gpuAt;
    @ApiModelProperty(value = "CPU갯수")
    private Integer cpuCo;
    @ApiModelProperty(value = "데이터저장소")
    private boolean dataStorgeAt;
    @ApiModelProperty(value = "사유(보완,반려)")
    private String rsndqf;
    @ApiModelProperty(value = "사용시작일")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일")
    private Date useEndDt;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;

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
