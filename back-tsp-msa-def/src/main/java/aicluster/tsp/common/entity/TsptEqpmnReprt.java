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
@ApiModel(description = "결과보고서")

public class TsptEqpmnReprt implements Serializable {

    private static final long serialVersionUID = -6369806312799506919L;

    @ApiModelProperty(value = "보고서ID")
    private String reprtId;
    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "결과상태")
    private String reprtSttus;
    @ApiModelProperty(value = "보완사유")
    private String rsndqf;
    @ApiModelProperty(value = "장비활용 활용목적")
    private String prcusePurps;
    @ApiModelProperty(value = "주관기관")
    private String mnnst;
    @ApiModelProperty(value = "대상장비")
    private String trgetEqpmn;
    @ApiModelProperty(value = "상세활용내역")
    private String detailPrcuse;
    @ApiModelProperty(value = "장비활용용도")
    private String prcusePrpos;
    @ApiModelProperty(value = "장비활용계획")
    private String prcusePlan;
    @ApiModelProperty(value = "장비활용내역")
    private String prcuseDtls;
    @ApiModelProperty(value = "계획대비차이점")
    private String dffrnc;
    @ApiModelProperty(value = "활용목적달성")
    private String achiv;
    @ApiModelProperty(value = "장비활용기대효과")
    private String expcEffect;
    @ApiModelProperty(value = "기술 제품출시 예상시기")
    private String cmtExpectEra;
    @ApiModelProperty(value = "기술 매출 예상액")
    private String expectSalamt;
    @ApiModelProperty(value = "주관기관 장비활용 시 좋았던 점")
    private String strength;
    @ApiModelProperty(value = "주관기관 장비활용 시 아쉬웠던 점")
    private String weakness;
    @ApiModelProperty(value = "첨부파일그룹ID")
    private String atchmnflGroupId;
    @ApiModelProperty(value = "생성자ID")
    private String creatrId;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "수정자ID")
    private String updusrId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;
    @ApiModelProperty(value = "장비활용분야")
    private String prcuseRealm;
    @ApiModelProperty(value = "장비활용 필요성")
    private String prcuseNeed;

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
