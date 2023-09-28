package aicluster.tsp.api.front.svcintro.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EqpmnIntroDetailParam implements Serializable {

    private static final long serialVersionUID = 2707512062497190659L;

    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "장비명(영문)")
    private String eqpmnNmEngl;
    @ApiModelProperty(value = "모델명")
    private String modelNm;
    @ApiModelProperty(value = "카테고리")
    private String eqpmnClNm;
    @ApiModelProperty(value = "규격")
    private String eqpmnStndrd;
    @ApiModelProperty(value = "전원")
    private String srcelct;
    @ApiModelProperty(value = "매뉴얼 여부")
    private String mnlAt;
    @ApiModelProperty(value = "소프트웨어 여부")
    private String swAt;
    @ApiModelProperty(value = "설치장소")
    private String legacyItlpc;
    @ApiModelProperty(value = "유료 여부")
    private String pchrgAt;
    @ApiModelProperty(value = "특기사항")
    private String spcmnt;
    @ApiModelProperty(value = "요약")
    private String sumry;
    @ApiModelProperty(value = "제원및 주요구성품")
    private String specComposition;
    @ApiModelProperty(value = "보조기기")
    private String asstnMhrls;
    @ApiModelProperty(value = "분야/용도")
    private String realmPrpos;
    @ApiModelProperty(value = "이미지")
    private String imageId;
    @ApiModelProperty(value = "자산번호")
    private String assetsNo;
}
