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
@ApiModel(description = "장비 사용관리 DTO")
public class EqpmnUseDto implements Serializable {
	
	private static final long serialVersionUID = -7854668734932139174L;
	@ApiModelProperty(value = "번호")
	Integer number;
	@ApiModelProperty(value = "신청ID")
	private String reqstID;
	@ApiModelProperty(value = "사용")
	private String useSttus;
	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "구분")
	private String mberDiv;
	@ApiModelProperty(value = "사업자명/이름")
	private String entrprsNm;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "장비명")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "사용 시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용 종료일")
	private Date useEndDt;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
	//@ApiModelProperty(value = "신청일시")
	//private Date creatDt;
	@ApiModelProperty(value = "반출여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "반입여부")
	private Boolean tkinAt;
	@ApiModelProperty(value = "반출심의결과")
	private String tkoutDlbrtResult;

	public String getExTkoutAt(){
		if(tkoutAt == null){
			return null;
		}
		if(tkoutAt){
			return "반출";
		}
		else {
			return "미반출";
		}
	}
	public String getExTkinAt(){
		if(tkinAt == null){
			return null;
		}
		if(tkinAt){
			return "반입완료";
		}
		else {
			return "반입전";
		}
	}

	public String getExUseSttus(){
		if(useSttus == null){
			return null;
		}
		return TspCode.eqpmnUsage.valueOf(useSttus).getTitle();
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