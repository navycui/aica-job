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
@ApiModel(description = "분석도구 신청 상세 DTO")
public class AnalsReqstDetailDto implements Serializable {

	private static final long serialVersionUID = 8126902709889699942L;
	@ApiModelProperty(value = "신청ID")
	private String reqstID;
	@ApiModelProperty(value = "사용상태")
	private String useSttus;
	@ApiModelProperty(value = "분석도구 구분")
	private String analsUntDiv;
	@ApiModelProperty(value = "구분")
	private String mberDiv;
	@ApiModelProperty(value = "사업자명/이름")
	private String entrprsNm;
	@ApiModelProperty(value = "직위")
	private String ofcps;
	@ApiModelProperty(value = "연락처")
	private String cttpc;
	@ApiModelProperty(value = "이메일")
	private String email;
	@ApiModelProperty(value = "AI 직억단지 사업참여 여부")
	private String partcptnDiv;
	@ApiModelProperty(value = "리소스Id")
	private String resrceId;
	@ApiModelProperty(value = "신청사유")
	private String reqstResn;
	@ApiModelProperty(value = "사용 시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용 종료일")
	private Date useEndDt;
	@ApiModelProperty(value = "신청일시")
	private Date creatDt;
	@ApiModelProperty(value = "신청자Id", hidden = true)
	private String creatrId;

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