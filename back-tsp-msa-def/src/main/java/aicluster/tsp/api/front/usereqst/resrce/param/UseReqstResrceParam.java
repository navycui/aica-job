package aicluster.tsp.api.front.usereqst.resrce.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UseReqstResrceParam implements Serializable {
    private static final long serialVersionUID = 2135932157438145922L;

//    @ApiModelProperty(value = "구분")
//    private String mberDiv;
//    @ApiModelProperty(value = "사업자명")
//    private String entrprsNm;
//    @ApiModelProperty(value = "사용자명")
//    private String userNm;
//    @ApiModelProperty(value = "직위")
//    private String ofcps;
//    @ApiModelProperty(value = "연락처")
//    private String cttpc;
//    @ApiModelProperty(value = "이메일")
//    private String email;
//    @ApiModelProperty(value = "AI 직접단지 사업참여 여부")
//    private String partcptnDiv;

//    @ApiModelProperty(value = "신청자ID")
//    private String applcntId;
//
//    @ApiModelProperty(value = "신청ID")
//    private String reqstId;
//    @ApiModelProperty(value = "신청상태")
//    private String reqstSttus;
//    @ApiModelProperty(value = "사용상태")
//    private String useSttus;
//    @ApiModelProperty(value = "접수번호")
//    private String rceptNo;
    @ApiModelProperty(value = "신청자ID")
    private String applcntId;
    @ApiModelProperty(value = "사업자등록 파일ID")
    private String bsnlcnsFileId;
    @ApiModelProperty(value = "AI직접단지 참여사업")
    private String partcptnDiv;
    @ApiModelProperty(value = "직위")
    private String ofcps;

    @ApiModelProperty(value = "CPU 갯수")
    private Integer cpuCo;
    @ApiModelProperty(value = "GPU사용 여부")
    private boolean gpuAt;
    @ApiModelProperty(value = "데이터 저장소")
    private boolean dataStorgeAt;
    @ApiModelProperty(value = "활용목적")
    private String useprps;
    @ApiModelProperty(value = "첨부 파일")
    private String atchmnflGroupId;

    @ApiModelProperty(value = "사용시작일")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일")
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
