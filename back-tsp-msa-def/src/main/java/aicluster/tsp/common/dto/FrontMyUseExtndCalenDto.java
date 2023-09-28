package aicluster.tsp.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "기간연장신청시 캘린더 필요 정보")
public class FrontMyUseExtndCalenDto implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;

    @ApiModelProperty(value = "사용시작시간")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료시간")
    private Date useEndDt;
    @ApiModelProperty(value = "예약불가")
    private List<String> notReservation;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyUseExtndReqstDt {
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

    @JsonIgnore
    List<MyUseExtndReqstDt> myUseExtndReqstDt;

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
    public List<String> getNotReservation() {
        List<String> notReservation = new ArrayList<>();
        if (this.notReservation != null) {
            notReservation.addAll(this.notReservation);
        }
        return notReservation;
    }
    public void setNotReservation(List<String> notReservation) {
        this.notReservation = new ArrayList<>();
        if (notReservation != null) {
            this.notReservation.addAll(notReservation);
        }
    }
}