package aicluster.tsp.api.admin.eqpmn.estmt.param;

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
@ApiModel(description = "견적요청 조회 리스트")
public class EstmtListParam implements Serializable {
	private static final long serialVersionUID = -7240134340483841154L;
	/**
	 * 
	 */
	@ApiModelProperty(value = "신청 상태")
	private String reqstSttus;
	@ApiModelProperty(value = "구분(기업, 개인, etc...)")
	private String mberDiv;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "회원명/사업자명")
	private String entrprsNm;
	@ApiModelProperty(value = "사용자")
	private String userNm;
	@ApiModelProperty(value = "접수번호(ER+8자리순번)")
	private String rceptNo;

	@ApiModelProperty(value = "신청일 시작(검색)")
	private Date creatBeginDt;
	@ApiModelProperty(value = "신청일 끝(검색)")
	private Date creatEndDt;

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

}