package aicluster.tsp.common.dto;

import aicluster.tsp.api.front.mypage.use.param.MyUseDetailParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "장비사용 상세 결과보고서 조회")
public class FrontMyUseReprtDto implements Serializable {
	private static final long serialVersionUID = -2105235904255249657L;

	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "장비명(영문)")
	private String eqpmnNmEngl;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "사용시작시간")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료시간")
	private Date useEndDt;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "예상사용시간")
	private Integer expectUsgtm;
	@ApiModelProperty(value = "예상사용료")
	private Integer expectRntfee;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "할인 ID")
	private String dscntId;
	@ApiModelProperty(value = "할인사유")
	private String dscntResn;
	@ApiModelProperty(value = "할인률(%)")
	private Integer dscntRate;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "시간당 사용료")
	private Integer rntfeeHour;
	@JsonIgnore
	@ApiModelProperty(value = "보고서ID")
	private String reprtId;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MyUseReprt {
		@ApiModelProperty(value = "보고서ID")
		private String reprtId;
		@ApiModelProperty(value = "결과상태")
		private String reprtSttus;
		@ApiModelProperty(value = "보완사유")
		private String rsndqf;
		@ApiModelProperty(value = "장비활용분야")
		private String prcuseRealm;
		@ApiModelProperty(value = "장비활용 활용목적")
		private String prcusePurps;
		@ApiModelProperty(value = "장비활용 필요성")
		private String prcuseNeed;
		@ApiModelProperty(value = "주관기관")
		private String mnnst;
		@ApiModelProperty(value = "대상장비")
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
		@ApiModelProperty(value = "첨부파일그룹ID")
		private String atchmnflGroupId;
		@ApiModelProperty(value = "활용목적달성")
		private String achiv;
		@ApiModelProperty(value = "장비활용기대효과")
		private String expcEffect;
		@ApiModelProperty(value = "기술 제품출시 예상시기")
		private String cmtExpectEra;
		@ApiModelProperty(value = "기술 매출 예상액")
		private String expectSalamt;
		@ApiModelProperty(value = "주관기관 장비활용 시 좋았던 점")
		private String strength;
		@ApiModelProperty(value = "주관기관 장비활용 시 아쉬웠던 점")
		private String weakness;
		@ApiModelProperty(value = "생성자ID")
		private String creatrId;
		@ApiModelProperty(value = "생성일시")
		private Date creatDt;
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

		List<MyUseDetailParam.MyAttachMentParam> myAttachMentParam;
	}
	MyUseReprt myUseReprt;

	List<FrontMyUseExtndCalenDto.MyUseExtndReqstDt> myUseExtndReqstDt;

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