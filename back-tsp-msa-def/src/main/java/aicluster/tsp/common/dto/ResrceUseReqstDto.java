package aicluster.tsp.common.dto;

import aicluster.tsp.common.util.TspCode;
import bnet.library.util.CoreUtils;
import io.swagger.annotations.Api;
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
@ApiModel(description = "실증 자원신청 DTO")
public class ResrceUseReqstDto implements Serializable {

    private static final long serialVersionUID = -661375127768153790L;

    @ApiModelProperty(value = "번호")
    private Integer number;
    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "구분")
    private String mberDiv;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "사업자명")
    private String entrprsNm;
    @ApiModelProperty(value = "사용자명")
    private String userNm;
    @ApiModelProperty(value = "사용시작일(검색)")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일(검색)")
    private Date useEndDt;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "신청시작일(검색)")
    private Date creatDt;

    public String getExReqstSttus(){
        if(reqstSttus == null){
            return null;
        }
        return TspCode.reqstSttus.valueOf(reqstSttus).getTitle();
    }
    public String getExUseSttus(){
        if(useSttus == null){
            return null;
        }
        return TspCode.reqUsage.valueOf(useSttus).getTitle();
    }
    public String getExMberDiv(){
        if(mberDiv == null){
            return null;
        }
        return TspCode.mberDiv.valueOf(mberDiv).getTitle();
    }
    public String getExCreatDt(){
        return CoreUtils.date.format(this.creatDt, "yyyy-MM-dd HH:mm:ss");
    }

    public String getExUseBeginDt(){
        return CoreUtils.date.format(this.useBeginDt, "yyyy-MM-dd HH:mm:ss");
    }

    public String getExUseEndDt(){
        return CoreUtils.date.format(this.useEndDt, "yyyy-MM-dd HH:mm:ss");
    }

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
