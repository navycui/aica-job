package aicluster.tsp.api.front.anals.param;

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
@ApiModel(description = "분석도구 템플릿")
public class FrontAnalsTmplatParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @JsonIgnore
    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @JsonIgnore
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;

    @ApiModelProperty(value = "분석도구 타입", hidden = true)
    private String analsUntDiv;
    @ApiModelProperty(value = "템플릿타입", hidden = true)
    private String tmplatTy;
    @ApiModelProperty(value = "사용가능 수", hidden = true)
    private String enabledCount;
    @ApiModelProperty(value = "설명", hidden = true)
    private String description;
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
