package aicluster.tsp.common.dto;

import bnet.library.util.CoreUtils;
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
@ApiModel(description = "마이페이지 분석도구 DTO")
public class FrontMyAnalsReqstDto implements Serializable {

	private static final long serialVersionUID = 8126902709889699942L;
	@ApiModelProperty(value = "신청ID")
	private String reqstId;
	@ApiModelProperty(value = "사용상태")
	private String useSttus;
	@ApiModelProperty(value = "분석도구 구분")
	private String analsUntDiv;
	@ApiModelProperty(value = "템플릿타입")
	private String tmplatTy;
//	@ApiModelProperty(value = "리소스Id")
//	private String resrceId;
	@ApiModelProperty(value = "사용 시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용 종료일")
	private Date useEndDt;
	@ApiModelProperty(value = "신청일시")
	private Date creatDt;
	@ApiModelProperty(value = "templat설명")
	private String description;

	@ApiModelProperty(value = "신청사유")
	private String reqstResn;
	@ApiModelProperty(value = "반려사유")
	private String rsndqf;

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