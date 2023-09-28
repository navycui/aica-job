package aicluster.tsp.api.admin.eqpmn.mngm.param;

import aicluster.tsp.common.util.TspCode;
import bnet.library.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "관리정보 교정/수리/점검 수정 후 정보 표시")
public class MngmMgtHistParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7093672717389634086L;
	@ApiModelProperty(value = "관리ID")
	private String manageId;
	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "관리시작일")
	private Date manageBeginDt;
	@ApiModelProperty(value = "관리종료일")
	private Date manageEndDt;
	@ApiModelProperty(value = "관리구분")
	private String manageDiv;
	@ApiModelProperty(value = "관리사유")
	private String manageResn;
	@ApiModelProperty(value = "관리결과")
	private String manageResult;
	@ApiModelProperty(value = "교정기관")
	private String crrcInstt;
	@ApiModelProperty(value = "처리자ID")
	private String opetrId;
	@ApiModelProperty(value = "생성자ID")
	private String creatrId;
	@JsonIgnore
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "수정자ID")
	private String updusrId;
	@JsonIgnore
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;
	@ApiModelProperty(value = "회원명")
	private String mberNm;


	public String getExManageDiv(){
		if(manageDiv == null){
			return null;
		}
		return TspCode.eqpmnSttus.valueOf(manageDiv).getTitle();
	}
	public String getExManageBeginDt(){
		return CoreUtils.date.format(this.manageBeginDt, "yyyy-MM-dd");
	}
	public String getExManageEndDt(){
		return CoreUtils.date.format(this.manageEndDt, "yyyy-MM-dd");
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
	public Date getManageBeginDt() {
		if (this.manageBeginDt != null) {
			return new Date(this.manageBeginDt.getTime());
		}
		return null;
	}
	public void setManageBeginDt(Date manageBeginDt) {
		this.manageBeginDt = null;
		if (manageBeginDt != null) {
			this.manageBeginDt = new Date(manageBeginDt.getTime());
		}
	}
	public Date getManageEndDt() {
		if (this.manageEndDt != null) {
			return new Date(this.manageEndDt.getTime());
		}
		return null;
	}
	public void setManageEndDt(Date manageEndDt) {
		this.manageEndDt = null;
		if (manageEndDt != null) {
			this.manageEndDt = new Date(manageEndDt.getTime());
		}
	}
}
