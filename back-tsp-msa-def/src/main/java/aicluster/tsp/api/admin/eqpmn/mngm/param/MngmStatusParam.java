package aicluster.tsp.api.admin.eqpmn.mngm.param;

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
@ApiModel(description = "장비사용 현황")
public class MngmStatusParam implements Serializable {

    private static final long serialVersionUID = 659383456080567213L;

    @ApiModelProperty(value = "번호")
    private Integer number;
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "자산번호")
    private String assetsNo;
    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "장비명(영문)")
    private String eqpmnNmEngl;
    @ApiModelProperty(value = "장비분류명")
    private String eqpmnClNm;


    @ApiModelProperty(value = "구분(기업, 개인, etc...)")
    private String mberDiv;
    @ApiModelProperty(value = "회원명/사업자명")
    private String userNm;
    @ApiModelProperty(value = "업체명")
    private String entrprsNm;

    @ApiModelProperty(value = "시작일")
    private Date useBeginDt;
    @ApiModelProperty(value = "종료일")
    private Date useEndDt;
    @ApiModelProperty(value = "시작일(엑셀)")
    private String exUseBeginDt;
    @ApiModelProperty(value = "종료일(엑셀)")
    private String exUseEndDt;

    @ApiModelProperty(value = "예상사용시간")
    private Integer expectUsgtm;




    public String getExMberDiv() {
        if (mberDiv == null) {
            return null;
        }
        return TspCode.mberDiv.valueOf(mberDiv).getTitle();
    }

    public Integer getExExpectUsgtm() {
        return expectUsgtm / 60;
    }

    public String getExUseBeginDt() {
        return CoreUtils.date.format(this.useBeginDt, "yyyy-MM-dd HH:mm:ss");
    }

    public String getExUseEndDt() {
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
