package aicluster.tsp.api.front.mypage.resrce.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "실증자원 목록리스트")
public class MyResrceDetailParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;

    @ApiModelProperty(value = "접수번호")
    private String rceptNo;
    @ApiModelProperty(value = "사용시작일")
    private Date creatDt;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "사용종료일")
    private Date useEndDt;
    @ApiModelProperty(value = "사용반환일")
    private Date useRturnDt;

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

    @ApiModelProperty(value = "GPU사용여부")
    private boolean gpuAt;
    @ApiModelProperty(value = "CPU갯수")
    private Integer cpuCo;
    @ApiModelProperty(value = "데이터저장소")
    private boolean dataStorgeAt;
    @ApiModelProperty(value = "활용목적")
    private String useprps;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyAttachMentParam {
        @ApiModelProperty(value = "첨부파일ID")
        private String attachmentId;
        @ApiModelProperty(value = "첨부파일 이름")
        private String fileNm;
        @ApiModelProperty(value = "첨부파일 용량")
        private Integer fileSize;
        @ApiModelProperty(value = "첨부파일 타입")
        private String contentType;
    }
    List<MyAttachMentParam> attachMentParam;

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
