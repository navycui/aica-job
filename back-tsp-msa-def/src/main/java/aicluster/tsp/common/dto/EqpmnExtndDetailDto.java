package aicluster.tsp.common.dto;

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
@ApiModel(description = "기간연장신청 상세정보")
public class EqpmnExtndDetailDto implements Serializable {
	private static final long serialVersionUID = -7240134340483841154L;

	@ApiModelProperty(value = "연장신청ID")
	private String etReqstId;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "입금안내문")
	private String rcpmnyGdcc;
	@ApiModelProperty(value = "입금안내일시")
	private Date rcpmnyGdccDt;
	@ApiModelProperty(value = "미납사유")	// Null 여부 확인
	private String npyResn;
	@ApiModelProperty(value = "사용신청ID")	// URL 보내기 위함
	private String reqstId;
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
	@ApiModelProperty(value = "기존사용시작일")	// 기존사용시작일
	private Date oldUseBeginDt;
	@ApiModelProperty(value = "기존사용종료일")	// 기존사용종료일
	private Date oldUseEndDt;
	@ApiModelProperty(value = "사용시작시간")	// 연장신청시작
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료시간")	// 연장신청시작
	private Date useEndDt;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "예상사용시간")
	private Integer expectUsgtm;
	@ApiModelProperty(value = "시간당 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "예상사용료")
	private Integer expectRntfee;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "할인ID")
	private String dscntId;
	@ApiModelProperty(value = "할인사유")
	private String dscntResn;
	@ApiModelProperty(value = "할인률(%)")
	private Integer dscntRate;
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DetailDscntParam {

		@ApiModelProperty(value = "할인 ID")
		private String dscntId;
		@ApiModelProperty(value = "할인사유")
		private String dscntResn;
		@ApiModelProperty(value = "할인률(%)")
		private Integer dscntRate;
	}
	List<DetailDscntParam> detailDscntParam;

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
	public Date getRcpmnyGdccDt() {
		if (this.rcpmnyGdccDt != null) {
			return new Date(this.rcpmnyGdccDt.getTime());
		}
		return null;
	}
	public void setRcpmnyGdccDt(Date rcpmnyGdccDt) {
		this.rcpmnyGdccDt = null;
		if (rcpmnyGdccDt != null) {
			this.rcpmnyGdccDt = new Date(rcpmnyGdccDt.getTime());
		}
	}
	public Date getOldUseBeginDt() {
		if (this.oldUseBeginDt != null) {
			return new Date(this.oldUseBeginDt.getTime());
		}
		return null;
	}
	public void setOldUseBeginDt(Date oldUseBeginDt) {
		this.oldUseBeginDt = null;
		if (oldUseBeginDt != null) {
			this.oldUseBeginDt = new Date(oldUseBeginDt.getTime());
		}
	}
	public Date getOldUseEndDt() {
		if (this.oldUseEndDt != null) {
			return new Date(this.oldUseEndDt.getTime());
		}
		return null;
	}
	public void setOldUseEndDt(Date oldUseEndDt) {
		this.oldUseEndDt = null;
		if (oldUseEndDt != null) {
			this.oldUseEndDt = new Date(oldUseEndDt.getTime());
		}
	}
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