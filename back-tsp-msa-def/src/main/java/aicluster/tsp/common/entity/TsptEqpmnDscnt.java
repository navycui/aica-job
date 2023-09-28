package aicluster.tsp.common.entity;

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
@ApiModel(description = "할인정보 사용되는 Entity")
public class TsptEqpmnDscnt implements Serializable {
	
	private static final long serialVersionUID = -27604036830350093L;

	@ApiModelProperty(value = "할인 ID")
	private String dscntId;
	@ApiModelProperty(value = "할인사유")
	private String dscntResn;
	@ApiModelProperty(value = "할인률(%)")
	private Integer dscntRate;
	@ApiModelProperty(value = "사용상태")
	private String useSttus;
	@ApiModelProperty(value = "생성자ID")
	private String creatrId;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "수정자ID")
	private String updusrId;
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;

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
