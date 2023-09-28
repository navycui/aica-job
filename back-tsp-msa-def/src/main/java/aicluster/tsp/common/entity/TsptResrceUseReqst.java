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
@ApiModel(description = "실증자원신청")

public class TsptResrceUseReqst implements Serializable {

    private static final long serialVersionUID = 5966141828770482283L;

    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "신청자ID")
    private String applcntId;
    @ApiModelProperty(value = "GPU사용여부")
    private boolean gpuAt;
    @ApiModelProperty(value = "CPU갯수")
    private Integer cpuCo;
    @ApiModelProperty(value = "데이터저장소")
    private boolean dataStorgeAt;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;
    @ApiModelProperty(value = "활용목적")
    private String useprps;
    @ApiModelProperty(value = "사유(보완,반려)")
    private String rsndqf;
    @ApiModelProperty(value = "사용시작일")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일")
    private Date useEndDt;
    @ApiModelProperty(value = "사용반환일")
    private Date useRturnDt;
    @ApiModelProperty(value = "생성자ID")
    private String creatrId;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "수정자ID")
    private String updusrId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "AI 집적단지 사업참여 여부")
    private String partcptnDiv;

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
    public Date getUseRturnDt() {
        if (this.useRturnDt != null) {
            return new Date(this.useRturnDt.getTime());
        }
        return null;
    }
    public void setUseRturnDt(Date useRturnDt) {
        this.useRturnDt = null;
        if (useRturnDt != null) {
            this.useRturnDt = new Date(useRturnDt.getTime());
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
