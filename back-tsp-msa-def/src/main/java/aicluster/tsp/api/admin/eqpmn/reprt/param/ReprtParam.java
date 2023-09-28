package aicluster.tsp.api.admin.eqpmn.reprt.param;

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
@ApiModel(description = "결과보고서 >  결과보고서 관리 검색 조건 Param")
public class ReprtParam implements Serializable {

	private static final long serialVersionUID = -6261759605011312106L;

	@ApiModelProperty(value = "장비대여신청상태(G:EQPMN_RENTAL_ST)")
	private String reprtSttus;


	@ApiModelProperty(value = "검색시작일")
	private Date creatBeginDt;
	@ApiModelProperty(value = "검색종료일")
	private Date creatEndDt;

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
}
