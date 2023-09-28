package aicluster.tsp.api.front.mypage.resrce.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "실증자원 목록 검색")
public class MyResrceSearchParam implements Serializable {
    private static final long serialVersionUID = -1323959025694310534L;

    @ApiModelProperty(value = "사용상태")
    private String[] sttus;
    @ApiModelProperty(value = "사용시작일")
    private Date beginDt;
    @ApiModelProperty(value = "사용종료일")
    private Date endDt;
    @JsonIgnore
    @ApiModelProperty(value = "로그인계정ID")
    private String mberId;

    public void setSttus(String[] sttus) {
        this.sttus = Arrays.copyOf(sttus, sttus.length);
    }
    public Date getBeginDt() {
		if (this.beginDt != null) {
			return new Date(this.beginDt.getTime());
		}
		return null;
	}
	public void setBeginDt(Date beginDt) {
		this.beginDt = null;
		if (beginDt != null) {
			this.beginDt = new Date(beginDt.getTime());
		}
	}
	public Date getEndDt() {
		if (this.endDt != null) {
			return new Date(this.endDt.getTime());
		}
		return null;
	}
	public void setEndDt(Date endDt) {
		this.endDt = null;
		if (endDt != null) {
			this.endDt = new Date(endDt.getTime());
		}
	}

}
