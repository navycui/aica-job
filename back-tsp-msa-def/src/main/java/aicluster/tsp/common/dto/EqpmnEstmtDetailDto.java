package aicluster.tsp.common.dto;

import aicluster.tsp.api.common.param.CommonAttachmentParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "견적신청 상세 정보")
public class EqpmnEstmtDetailDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4206469873615590349L;
	@ApiModelProperty(value = "장비사용신청ID")
	private String reqstId;

	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
	@ApiModelProperty(value = "구분(기업, 개인, etc...)")
	private String mberDiv;
	@ApiModelProperty(value = "회원명/사업자명")
	private String entrprsNm;
	@ApiModelProperty(value = "신청자 이름")
	private String userNm;
	@ApiModelProperty(value = "직위")
	private String ofcps;
	@ApiModelProperty(value = "연락처")
	private String cttpc;
	@ApiModelProperty(value = "email")
	private String email;
	@ApiModelProperty(value = "ai직접단지 사업참여 여부")
	private String partcptnDiv;
	@ApiModelProperty(value = "장비Id")
	private String eqpmnId;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "장비명(영문)")
	private String eqpmnNmEngl;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "활용목적")
	private String useprps;
	@ApiModelProperty(value = "반출신청 여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "반출지 주소")
	private String tkoutAdres;
	@ApiModelProperty(value = "반출 사유")
	private String tkoutResn;
	@ApiModelProperty(value = "사용시작시간")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료시간")
	private Date useEndDt;
	@ApiModelProperty(value = "1시간 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "1일 가용시간")
	private Integer usefulHour;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "예상사용시간")
	private Integer expectUsgtm;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "예상사용료")
	private Integer expectRntfee;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "할인 ID")
	private String dscntId;
	@ApiModelProperty(value = "할인률")
	private Integer dscntRate;
	@ApiModelProperty(value = "할인 사유")
	private String dscntResn;
	@ApiModelProperty(value = "첨부파일 ID")
	private String atchmnflGroupId;
	@ApiModelProperty(value = "신청자 ID", hidden = true)
	private String applcntId;
	@ApiModelProperty(value = "보완사유")
	private String rsndqf;

	@ApiModelProperty(value = "할인 적용")
	private List<EqpmnDscntListDto> dscntList;

	@ApiModelProperty(value = "첨부파일 정보")
	private List<CommonAttachmentParam> attachmentList;
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
	public List<EqpmnDscntListDto> getDscntList() {
		List<EqpmnDscntListDto> dscntList = new ArrayList<>();
		if (this.dscntList != null) {
			dscntList.addAll(this.dscntList);
		}
		return dscntList;
	}
	public void setDscntList(List<EqpmnDscntListDto> dscntList) {
		this.dscntList = new ArrayList<>();
		if (dscntList != null) {
			this.dscntList.addAll(dscntList);
		}
	}
	public List<CommonAttachmentParam> getAttachmentList() {
		List<CommonAttachmentParam> attachmentList = new ArrayList<>();
		if (this.attachmentList != null) {
			attachmentList.addAll(this.attachmentList);
		}
		return attachmentList;
	}
	public void setAttachmentList(List<CommonAttachmentParam> attachmentList) {
		this.attachmentList = new ArrayList<>();
		if (attachmentList != null) {
			this.attachmentList.addAll(attachmentList);
		}
	}
}

