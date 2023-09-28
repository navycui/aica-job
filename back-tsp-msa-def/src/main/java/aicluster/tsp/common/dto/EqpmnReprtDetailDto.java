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
@ApiModel(description = "장비 사용관리 상세 DTO")
public class EqpmnReprtDetailDto implements Serializable {
	
	private static final long serialVersionUID = -2705280727316502404L;
	@ApiModelProperty(value = "신청일시")
	private Date creatDt;
	@ApiModelProperty(value = "사용상태")
	private String reprtSttus;
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
	@ApiModelProperty(value = "활용분야")
	private String prcuseRealm;
	@ApiModelProperty(value = "장비명 (국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "장비명 (영문)")
	private String eqpmnNmEngl;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;

	@ApiModelProperty(value = "1시간 사용료")
	private String rntfeeHour;
	//@ApiModelProperty(value = "1일 가용시간")
	//private String usefulHourDay;
	@ApiModelProperty(value = "선납")
	private String pymntMth;
	@ApiModelProperty(value = "시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "종료일")
	private Date useEndDt;
	@ApiModelProperty(value = "사용시간")
	private String usgtm;
	@ApiModelProperty(value = "할인사유")
	private String dscntResn;
	@ApiModelProperty(value = "할인률")
	private String dscntRate;
	@ApiModelProperty(value = "할인적용금액")
	private String rntfee;

	@ApiModelProperty(value = "장비활용 활용목적")
	private String prcusePurps;
	@ApiModelProperty(value = "장비활용 필요성")
	private String prcuseNeed;
	@ApiModelProperty(value = "주관기관")
	private String mnnst;
	@ApiModelProperty(value = "대상장비")			//??
	private String trgetEqpmn;
	@ApiModelProperty(value = "상세활용내역")
	private String detailPrcuse;
	@ApiModelProperty(value = "장비활용용도")
	private String prcusePrpos;
	@ApiModelProperty(value = "장비활용계획")
	private String prcusePlan;
	@ApiModelProperty(value = "장비활용내역")
	private String prcuseDtls;
	@ApiModelProperty(value = "계획대비차이점")
	private String dffrnc;
	@ApiModelProperty(value = "첨부파일 그룹ID")
	private String atchmnflGroupId;
	@ApiModelProperty(value = "활용목적달성")
	private String achiv;
	@ApiModelProperty(value = "장비활용기대효과")
	private String expcEffect;
	@ApiModelProperty(value = "기술 제품출시 예상시기(년월?)")
	private String cmtExpectEra;
	@ApiModelProperty(value = "기술 매출 예상액")
	private String expectSalamt;
	@ApiModelProperty(value = "주관기관 장비활용 시 좋았던 점")
	private String strength;
	@ApiModelProperty(value = "주관기관 장비활용 시 아쉬웠던 점")
	private String weakness;
	@ApiModelProperty(value = "신청자 ID", hidden = true)
	private String applcntId;

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