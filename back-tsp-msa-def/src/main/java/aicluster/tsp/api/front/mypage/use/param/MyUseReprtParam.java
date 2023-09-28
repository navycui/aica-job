package aicluster.tsp.api.front.mypage.use.param;

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
@ApiModel(description = "장비사용 상세 기간연장신청")
public class MyUseReprtParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;

    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "사용시간")
    private Integer usgtm;
    @ApiModelProperty(value = "1시간 사용료")
    private Integer rntfeeHour;
    @ApiModelProperty(value = "사용료")
    private Integer rntfee;
    @ApiModelProperty(value = "할인금액")
    private Integer dscntRntfee;
    @ApiModelProperty(value = "지불방법")
    private String pymntMth;

    @JsonIgnore
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @JsonIgnore
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @JsonIgnore
    @ApiModelProperty(value = "할인ID")
    private String dscntId;
    @JsonIgnore
    @ApiModelProperty(value = "할인률(%)")
    private Integer dscntRate;
    @JsonIgnore
    @ApiModelProperty(value = "반출여부")
    private boolean tkoutAt;

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
