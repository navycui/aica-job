package aicluster.tsp.api.admin.eqpmn.mngm.param;

import aicluster.tsp.common.dto.EqpmnExtndDetailDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "MngmDetailParam")
public class MngmDetailParam implements Serializable {
	private static final long serialVersionUID = 8269672170064333938L;
	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "장비명(영문)")
	private String eqpmnNmEngl;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "장비분류ID")
	private String eqpmnClId;
	@ApiModelProperty(value = "장비규격")
	private String eqpmnStndrd;
	@ApiModelProperty(value = "요약")
	private String sumry;
	@ApiModelProperty(value = "제원 및 주요구성품")
	private String specComposition;
	@ApiModelProperty(value = "보조기기")
	private String asstnMhrls;
	@ApiModelProperty(value = "분야/용도")
	private String realmPrpos;
	@ApiModelProperty(value = "이미지파일ID")
	private String imageId;
	@ApiModelProperty(value = "전원")
	private String srcelct;
	@ApiModelProperty(value = "매뉴얼유무")
	private boolean mnlAt;
	@ApiModelProperty(value = "소프트웨어유무")
	private boolean swAt;
	@ApiModelProperty(value = "기존설치장소")
	private String legacyItlpc;
	@ApiModelProperty(value = "유료여부")
	private boolean pchrgAt;
	@ApiModelProperty(value = "특기사항")
	private String spcmnt;
	@ApiModelProperty(value = "AS업체")
	private String asEntrprsNm;
	@ApiModelProperty(value = "AS담당자")
	private String asChargerNm;
	@ApiModelProperty(value = "AS연락처(전화)")
	private String asChargerCttpc;
	@ApiModelProperty(value = "구입일")
	private Date purchsDt;
	@ApiModelProperty(value = "구입처")
	private String strNm;
	@ApiModelProperty(value = "구입가격(원)")
	private Long purchsPc;
	@ApiModelProperty(value = "제조사")
	private String makr;
	@ApiModelProperty(value = "반출여부")
	private boolean tkoutAt;
	@ApiModelProperty(value = "생성자ID(CMMT_MEMBER.MEMBER_ID)")
	private String creatrId;
	@JsonIgnore
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "수정자ID(CMMT_MEMBER.MEMBER_ID)")
	private String updusrId;
	@JsonIgnore
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class EqpmnClParam {
		@ApiModelProperty(value = "장비분류 ID")
		private String eqpmnClId;
		@ApiModelProperty(value = "부모장비분류 ID")
		private String eqpmnLclasId;

		@ApiModelProperty(value = "정렬 순서")
		private Integer ordr;
		@ApiModelProperty(value = "장비분류명")
		private String eqpmnClNm;
		@ApiModelProperty(value = "분류값")
		private Integer depth;
		@ApiModelProperty(value = "사용여부")
		private boolean useAt;

	}
	List<EqpmnExtndDetailDto.DetailDscntParam> DetailDscntParam;
	List<EqpmnClParam> eqpmnClParam;
	public Date getPurchsDt() {
		if (this.purchsDt != null) {
			return new Date(this.purchsDt.getTime());
		}
		return null;
	}
	public void setPurchsDt(Date purchsDt) {
		this.purchsDt = null;
		if (purchsDt != null) {
			this.purchsDt = new Date(purchsDt.getTime());
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
}