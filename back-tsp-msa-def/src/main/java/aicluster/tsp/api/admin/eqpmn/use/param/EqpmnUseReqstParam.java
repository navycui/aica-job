package aicluster.tsp.api.admin.eqpmn.use.param;

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
@ApiModel(description = "장비 대여 >  장비신청 관리 검색 조건 Param")
public class EqpmnUseReqstParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6957747613328915522L;
	@ApiModelProperty(value = "장비대여신청상태(G:EQPMN_RENTAL_ST)")
	private String reqstSttus;
	@ApiModelProperty(value = "신청일 (시작)")
	private Date creatBeginDt;
	@ApiModelProperty(value = "신청일 (끝)")
	private Date creatEndDt;
	@ApiModelProperty(value = "반출 여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "지불방법(G:PAYMENT_METHOD)")
	private String pymntMth;

	@ApiModelProperty(value = "사용시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료일")
	private Date useEndDt;

	@ApiModelProperty(value = "장비명")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "이름/사업자명")
	private String entrprsNm;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;

	public Date getCreatBeginDt() {
        if (this.creatBeginDt != null) {
            return new Date(this.creatBeginDt.getTime());
        }
        return null;
    }
	public void setCreatBeginDt(Date creatBeginDt) {
        this.creatBeginDt = null;
        if (creatBeginDt != null) {
            this.creatBeginDt = new Date(creatBeginDt.getTime());
        }
    }
	public Date getCreatEndDt() {
        if (this.creatEndDt != null) {
            return new Date(this.creatEndDt.getTime());
        }
        return null;
    }
	public void setCreatEndDt(Date creatEndDt) {
        this.creatEndDt = null;
        if (creatEndDt != null) {
            this.creatEndDt = new Date(creatEndDt.getTime());
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
