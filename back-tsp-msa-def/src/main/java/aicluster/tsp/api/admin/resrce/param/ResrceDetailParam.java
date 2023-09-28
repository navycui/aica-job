package aicluster.tsp.api.admin.resrce.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResrceDetailParam implements Serializable {

    private static final long serialVersionUID = 5744195219628516574L;
    @ApiModelProperty(value = "신청자ID")
    private String applcntId;

    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "신청일")
    private Date creatDt;
    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "반환요청일")
    private Date useRturnDt;

    @ApiModelProperty(value = "구분")
    private String mberDiv;
    @ApiModelProperty(value = "사업자명")
    private String entrprsNm;
    @ApiModelProperty(value = "이름")
    private String userNm;
    @ApiModelProperty(value = "직위")
    private String ofcps;
    @ApiModelProperty(value = "연락처")
    private String cttpc;
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "AI 집적단지 사업참여 여부")
    private String partcptnDiv;

    @ApiModelProperty(value = "GPU 사용여부")
    private boolean gpuAt;
    @ApiModelProperty(value = "CPU 갯수")
    private Integer cpuCo;
    @ApiModelProperty(value = "데이터저장소 사용여부")
    private boolean dataStorgeAt;

    @ApiModelProperty(value = "활용목적")
    private String useprps;

    @ApiModelProperty(value = "첨부파일")
    private String atchmnflGroupId;

    @ApiModelProperty(value = "사유")
    private String rsndqf;

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
    public Date getUseRturnDt() {
        if (this.useRturnDt != null) {
            return new Date(this.useRturnDt.getTime());
        }
        return null;
    }
    public void setUseRturnDt(Date useRturnDt) {
        this.useRturnDt = null;
        if (useRturnDt != null) {
            this.useRturnDt = new Date(useRturnDt.getTime());
        }
    }
}
