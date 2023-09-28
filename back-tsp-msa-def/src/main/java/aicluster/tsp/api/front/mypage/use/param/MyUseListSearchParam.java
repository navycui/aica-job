package aicluster.tsp.api.front.mypage.use.param;

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
@ApiModel(description = "장비사용 목록 검색 리스트")
public class MyUseListSearchParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "신청상태")
    private String[] reqstSttus;
    @ApiModelProperty(value = "사용상태")
    private String[] useSttus;
    @ApiModelProperty(value = "신청 시작일")
    private Date creatBeginDt;
    @ApiModelProperty(value = "신청 종료일")
    private Date creatEndDt;
    @ApiModelProperty(value = "로그인계정ID")
    private String mberId;

    public void setReqstSttus(String[] reqstSttus) {
        this.reqstSttus = Arrays.copyOf(reqstSttus, reqstSttus.length);
    }
    public void setUseSttus(String[] useSttus) {
        this.useSttus = Arrays.copyOf(useSttus, useSttus.length);
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
