package aicluster.tsp.api.front.usereqst.estmt.param;

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
@ApiModel(description = "견적신청")
public class UseReqstEstmtParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "견전신청Id", hidden = true)
    private String estmtId;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "신청자Id", hidden = true)
    private String creatrId;
    @ApiModelProperty(value = "장비Id")
    private String eqpmnId;
    @ApiModelProperty(value = "신청상태", hidden = true)
    private String reqstSttus;
    @ApiModelProperty(value = "지불방법")
    private String pymntMth;
    @ApiModelProperty(value = "사용료")
    private Integer rntfee;
    @ApiModelProperty(value = "예상사용료")
    private Integer expectRntfee;
    @ApiModelProperty(value = "예상사용시간")
    private Integer expectUsgtm;
    @ApiModelProperty(value = "할인금액")
    private Integer dscntAmount;
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

    @ApiModelProperty(value = "AI직접단지 참여사업")
    private String partcptnDiv;
    @ApiModelProperty(value = "첨부파일 ID")
    private String atchmnflGroupId;
    @ApiModelProperty(value = "서약서 파일ID")
    private String promsAtchmnflId;

    @ApiModelProperty(value = "직위")
    private String ofcps;
    @ApiModelProperty(value = "사업자등록 파일ID")
    private String bsnlcnsFileId;
    @ApiModelProperty(value = "반입여부")
    private boolean tkinAt;

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
