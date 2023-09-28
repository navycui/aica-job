package aicluster.tsp.api.admin.eqpmn.estmt.param;

import aicluster.tsp.common.entity.TsptEqpmnDscnt;
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
@ApiModel(description = "견적요청 상세 > 신청정보 신청정보")
public class EstmtDetailParam implements Serializable {
    private static final long serialVersionUID = -4257184165675599473L;

    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "장비견적신청상태(G:EQPMN_ESTMT_ST)")
    private String reqstSttus;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "접수번호(ER+8자리순번)")
    private String rceptNo;
    @ApiModelProperty(value = "구분(기업, 개인, etc...)")
    private String mberDiv;
    @ApiModelProperty(value = "회원명/사업자명")
    private String entrprsNm;
    @ApiModelProperty(value = "직급")
    private String ofcps;
    @ApiModelProperty(value = "암호화된 전화번호")
    private String cttpc;
    @ApiModelProperty(value = "암호화된 이메일")
    private String email;
    @ApiModelProperty(value = "사업참여여부")
    private boolean column1;
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
    @ApiModelProperty(value = "반출여부")
    private boolean tkoutAt;
    @ApiModelProperty(value = "반출지주소")
    private String tkoutAdres;
    @ApiModelProperty(value = "반출사유")
    private String tkoutResn;
    @ApiModelProperty(value = "시작일시")
    private Date useBeginDt;
    @ApiModelProperty(value = "종료일시")
    private Date useEndDt;
    @ApiModelProperty(value = "시간당사용료(원)")
    private Long rntfeeHour;
    @ApiModelProperty(value = "1일 가용 시작시간")
    private Long usefulBeginHour;
    @ApiModelProperty(value = "1일 가용 종료시간")
    private Long usefulEndHour;
    @ApiModelProperty(value = "지불방법(G:PAYMENT_METHOD)")
    private String pymntMth;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;

    @ApiModelProperty(value = "생성자ID(CMMT_MEMBER.MEMBER_ID)")
    private String creatorId;
    @ApiModelProperty(value = "수정자ID(CMMT_MEMBER.MEMBER_ID)")
    private String updaterId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;



    //	@Data
//	@Builder
//	@AllArgsConstructor
//	@NoArgsConstructor
//	public static class EstmtDetailModifyPrice {
//		@JsonIgnore
//		@ApiModelProperty(value = "장비ID")
//		private String eqpmnId;
//		@JsonIgnore
//		@ApiModelProperty(value = "장비견적신청ID")
//		private String eqpmnEstmtId;
//		@ApiModelProperty(value = "조정 사용시간")
//		private Long modifyTime;
//		@ApiModelProperty(value = "장비할인조건ID")
//		private String eqpmnDscntCndId;
//		@ApiModelProperty(value = "사용금액(시간당이용료 x 조정 사용시간)")
//		private Long usePrice;
//		@ApiModelProperty(value = "할인금액(사용금액 / 할인율(%))")
//		private Long dscntAmt;
//		@ApiModelProperty(value = "할인 적용금액(사용료)=(사용금액 - 할인금액)")
//		private Long rntfee;
//	}
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstmtDetailModifyPrice {

        //@JsonIgnore
        @ApiModelProperty(value = "장비견적신청ID")
        private String estmtId;
        @ApiModelProperty(value = "조정 사용시간")
        private Long usgtm;
        @ApiModelProperty(value = "장비할인조건ID")
        private String dscntId;
        @ApiModelProperty(value = "수정자ID")
        private String updusrId;
        @ApiModelProperty(value = "수정일시")
        private Date updtDt;

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

    EstmtDetailModifyPrice estmtDetailModifyPrice;
    List<TsptEqpmnDscnt> tsptEqpmnDscnt;

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