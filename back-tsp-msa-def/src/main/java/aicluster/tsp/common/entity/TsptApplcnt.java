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
@ApiModel(description = "신청자")

public class TsptApplcnt implements Serializable{

    private static final long serialVersionUID = 8640648318397554270L;

    @ApiModelProperty(value = "신청자ID")
    private String applcntId;
    @ApiModelProperty(value = "구분(개인, 기업)")
    private String mberDiv;
    @ApiModelProperty(value = "업체명")
    private String entrprsNm;
    @ApiModelProperty(value = "사용자")
    private String userNm;
    @ApiModelProperty(value = "직위")
    private String ofcps;
    @ApiModelProperty(value = "연락처")
    private String cttpc;
    @ApiModelProperty(value = "email")
    private String email;
    @ApiModelProperty(value = "AI직접단지 참여사업")
    private String partcptnDiv;
    @ApiModelProperty(value = "사업자등록파일ID")
    private String bsnlcnsFileId;
    @ApiModelProperty(value = "개인정보수집동의여부")
    private boolean indvdlinfoColctAgreAt;
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
