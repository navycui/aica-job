package aicluster.tsp.common.dto;

import aicluster.tsp.common.util.TspCode;
import bnet.library.util.CoreUtils;
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
@ApiModel(description = "장비목록 조회 리스트")
public class EqpmnAllListDto implements Serializable {

	private static final long serialVersionUID = 8663221660836298447L;

	@ApiModelProperty(value = "번호")
	private Integer number;

	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "장비상태(G:EQPMN_ST)")
	private String eqpmnSttus;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "분류")
	private String eqpmnClNm;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "반출여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "불용여부")
	private Boolean disuseAt;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;


	public String getExTkoutAt(){
		if(tkoutAt == null){
			return null;
		}
		if(tkoutAt){
			return "반출";
		}
		else {
			return "미반출";
		}
	}
	public String getExDisuseAt(){
		if(disuseAt == null){
			return null;
		}
		if(disuseAt){
			return "불용";
		}
		else {
			return "정상";
		}
	}

	public String getExEqpmnSttus(){
		if(eqpmnSttus == null){
			return null;
		}
		return TspCode.eqpmnSttus.valueOf(eqpmnSttus).getTitle();
	}

	public String getExCreatDt() {
		return CoreUtils.date.format(this.creatDt, "yyyy-MM-dd");
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