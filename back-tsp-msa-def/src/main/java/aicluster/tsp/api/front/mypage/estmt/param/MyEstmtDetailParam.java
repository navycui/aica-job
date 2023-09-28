package aicluster.tsp.api.front.mypage.estmt.param;

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
@ApiModel(description = "견적요청 상세정보")
public class MyEstmtDetailParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;
    @ApiModelProperty(value = "장비사용신청ID")
    private String reqstId;
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "견적ID")
    private String estmtId;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "장비명(영문)")
    private String eqpmnNmEngl;
    @ApiModelProperty(value = "AI직접단지 참여사업")
    private String partcptnDiv;
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
    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "시간당 사용료")
    private Integer rntfeeHour;
    @ApiModelProperty(value = "1일 가용시작시간")
    private Integer usefulBeginHour;
    @ApiModelProperty(value = "1일 가용종료시간")
    private Integer usefulEndHour;
    @ApiModelProperty(value = "예상사용료")
    private Integer expectRntfee;
    @ApiModelProperty(value = "사용료")
    private Integer rntfee;
    @ApiModelProperty(value = "지불방법")
    private String pymntMth;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;
    @ApiModelProperty(value = "1일 가용시간")
    private Integer usefulHour;
    @ApiModelProperty(value = "보완사유")
    private String rsndqf;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyAttachMentParam {
        @ApiModelProperty(value = "첨부파일ID")
        private String attachmentId;
        @ApiModelProperty(value = "첨부파일 이름")
        private String fileNm;
        @ApiModelProperty(value = "첨부파일 용량")
        private Integer fileSize;
        @ApiModelProperty(value = "첨부파일 타입")
        private String contentType;
    }
    List<MyAttachMentParam> myAttachMentParam;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstApproveDt {
        @ApiModelProperty(value = "처리구분")
        private String processKnd;
        @ApiModelProperty(value = "처리내용")
        private String processResn;
        @ApiModelProperty(value = "생성일자")
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
    }
    EstApproveDt estApproveDt;

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
