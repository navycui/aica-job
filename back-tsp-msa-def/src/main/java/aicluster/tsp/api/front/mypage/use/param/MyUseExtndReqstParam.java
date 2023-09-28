package aicluster.tsp.api.front.mypage.use.param;

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
@ApiModel(description = "장비사용 상세 기간연장 신청시 신청 일자")
public class MyUseExtndReqstParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;


    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;

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
