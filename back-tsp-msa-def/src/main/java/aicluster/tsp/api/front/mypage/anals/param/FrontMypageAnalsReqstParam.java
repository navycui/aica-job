package aicluster.tsp.api.front.mypage.anals.param;

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
@ApiModel(description = "분석도구 신청")
public class FrontMypageAnalsReqstParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "견전신청Id", hidden = true)
    private String reqstId;
    @ApiModelProperty(value = "신청자Id", hidden = true)
    private String creatrId;
    @ApiModelProperty(value = "분석도구 사용상태")
    private String[] useSttus;
    @ApiModelProperty(value = "신청 시작일")
    private Date creatBeginDt;
    @ApiModelProperty(value = "신청 종료일")
    private Date creatEndDt;

    public void setUseSttus(String[] useSttus) {
        this.useSttus = Arrays.copyOf(useSttus, useSttus.length);
    }
    public void setCreatBeginDt(Date creatBeginDt) {
        this.creatBeginDt = null;
        if (creatBeginDt != null) {
            this.creatBeginDt = new Date(creatBeginDt.getTime());
        }
    }
    public Date getCreatBeginDt() {
        if (this.creatBeginDt != null) {
            return new Date(this.creatBeginDt.getTime());
        }
        return null;
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
