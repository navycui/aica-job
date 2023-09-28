package aicluster.tsp.api.front.mypage.use.param;

import aicluster.tsp.api.common.param.CommonAttachmentParam;
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
@ApiModel(description = "장비사용 상세정보")
public class MyUseDetailParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;

    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @JsonIgnore
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "AI직접단지 참여사업")
    private String partcptnDiv;
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
    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "1시간 사용료")
    private Integer rntfeeHour;
    @ApiModelProperty(value = "1일 가용시작시간")
    private Integer usefulBeginHour;
    @ApiModelProperty(value = "1일 가용종료시간")
    private Integer usefulEndHour;
    @ApiModelProperty(value = "예상사용료")
    private Integer expectRntfee;
    @ApiModelProperty(value = "지불방법")
    private String pymntMth;
    @ApiModelProperty(value = "예상사용시간")
    private Integer expectUsgtm;
    @ApiModelProperty(value = "사용시간")
    private Integer usgtm;
    @ApiModelProperty(value = "1일 가용시간")
    private Integer usefulHour;

    @ApiModelProperty(value = "서약서ID")
    private String promsAtchmnflId;

    CommonAttachmentParam promsFileInfo;

    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;
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
    List<MyAttachMentParam> attachMentParam;

    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyUseApproveParam {
        @ApiModelProperty(value = "반출심의결과")
        private String tkoutDlbrtResult;
        @ApiModelProperty(value = "반출심의내용")
        private String tkoutDlbrtCn;
        @ApiModelProperty(value = "사용료")
        private Integer rntfee;
        @ApiModelProperty(value = "할인금액")
        private Integer dscntAmount;
        @ApiModelProperty(value = "추가금액")
        private Integer aditRntfee;
        @ApiModelProperty(value = "청구사유")
        private String rqestResn;
        @ApiModelProperty(value = "사용시간")
        private Integer usgtm;
        @ApiModelProperty(value = "예상사용시간")
        private Integer expectUsgtm;
        @ApiModelProperty(value = "할인ID")
        private String dscntId;
        @ApiModelProperty(value = "할인사유")
        private String dscntResn;
        @ApiModelProperty(value = "할인률(%)")
        private Integer dscntRate;

        @ApiModelProperty(value = "반입여부")
        private boolean tkinAt;
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MyUseDetailTkinAtParam {
            @ApiModelProperty(value = "구분")
            private String processKnd;
            @ApiModelProperty(value = "반입여부 생성일시")
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
        MyUseDetailTkinAtParam tkinAtParam;

    }
    MyUseApproveParam approveParam;

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
