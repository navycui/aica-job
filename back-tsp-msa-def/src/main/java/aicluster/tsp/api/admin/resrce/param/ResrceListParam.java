package aicluster.tsp.api.admin.resrce.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResrceListParam implements Serializable {

    private static final long serialVersionUID = -148577672071583655L;

    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "구분")
    private String mberDiv;
    @ApiModelProperty(value = "사업자명")
    private String entrprsNm;
    @ApiModelProperty(value = "사용자명")
    private String userNm;
    @ApiModelProperty(value = "사용시작일(검색)")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일(검색)")
    private Date useEndDt;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "신청시작일(검색)")
    private Date creatBeginDt;
    @ApiModelProperty(value = "신청종료일(검색)")
    private Date creatEndDt;

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
