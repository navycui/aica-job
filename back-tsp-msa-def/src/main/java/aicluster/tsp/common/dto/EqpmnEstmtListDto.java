package aicluster.tsp.common.dto;

import aicluster.tsp.common.util.TspCode;
import bnet.library.util.CoreUtils;
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
@ApiModel(description = "견적신청")
public class EqpmnEstmtListDto implements Serializable {
	private static final long serialVersionUID = 1020774100926119489L;
	/**
	 * 
	 */
	@ApiModelProperty(value = "번호")
	private Integer number;

	@ApiModelProperty(value = "신청ID")
	private String estmtID;
	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "구분(기업, 개인, etc...)")
	private String mberDiv;
	@ApiModelProperty(value = "회원명/사업자명")
	private String entrprsNm;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "사용시작시간")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료시간")
	private Date useEndDt;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;

	public String getExReqstSttus(){
		if(reqstSttus == null){
			return null;
		}
		return TspCode.reqstSttus.valueOf(reqstSttus).getTitle();
	}
	public String getExMberDiv(){
		if(mberDiv == null){
			return null;
		}
		return TspCode.mberDiv.valueOf(mberDiv).getTitle();
	}
	public String getExPymntMth(){
		if(pymntMth == null){
			return null;
		}
		return TspCode.pymntMth.valueOf(pymntMth).getTitle();
	}

	public String getExUseBeginDt(){
		return CoreUtils.date.format(this.useBeginDt, "yyyy-MM-dd HH:mm:ss");
	}
	public String getExUseEndDt(){
		return CoreUtils.date.format(this.useEndDt, "yyyy-MM-dd HH:mm:ss");
	}
	public String getExCreatDt(){
		return CoreUtils.date.format(this.creatDt, "yyyy-MM-dd HH:mm:ss");
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