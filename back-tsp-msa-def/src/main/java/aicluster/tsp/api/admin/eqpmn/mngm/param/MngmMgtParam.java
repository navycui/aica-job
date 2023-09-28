package aicluster.tsp.api.admin.eqpmn.mngm.param;

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
@ApiModel(description = "장비정보 관리정보")
public class MngmMgtParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -142062902522964963L;
		@ApiModelProperty(value = "장비ID")
		private String eqpmnId;
		@ApiModelProperty(value = "장비명")
		private String eqpmnNmKorean;
		@ApiModelProperty(value = "장비분류ID")
		private String eqpmnClId;
		@ApiModelProperty(value = "생성일시")
		private Date creatDt;
		@ApiModelProperty(value = "장비상태(G:EQPMN_ST)")
		private String eqpmnSttus;
		@ApiModelProperty("불용여부")
		private boolean disuseAt;
		@ApiModelProperty(value = "시간당 사용료")
		private Integer rntfeeHour;
		@ApiModelProperty(value = "1일 가용시작시간")
		private Integer usefulBeginHour;
		@ApiModelProperty(value = "1일 가용종료시간")
		private Integer usefulEndHour;
		@ApiModelProperty(value = "사용율 저조설정")
		private boolean useRateInctvSetupAt;
		@ApiModelProperty(value = "법정공휴일 휴일포함")
		private boolean hldyInclsAt;
		@ApiModelProperty(value = "반출시 휴일포함")
		private boolean tkoutHldyInclsAt;
		@ApiModelProperty(value = "미반출시 휴일포함")
		private boolean nttkoutHldyInclsAt;
		@ApiModelProperty(value = "점검대상여부")
		private boolean chckTrgetAt;
		@ApiModelProperty(value = "반출여부")
		private boolean tkoutAt;
		@ApiModelProperty("수리ID")
		private String repairId;
		@ApiModelProperty(value = "교정대상여부")
		private boolean crrcTrgetAt;
		@ApiModelProperty(value = "교정주기(일)")
		private Integer crrcCycle;
		@JsonIgnore
		@ApiModelProperty("교정ID")
		private String crrcId;
		@ApiModelProperty(value = "신청자ID")
		private String applcntId;


		@Data
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		public static class TkoutParam {
			@ApiModelProperty(value = "반출여부")
			private boolean tkoutAt;
			@ApiModelProperty(value = "반출상태")
			private String tkoutSttus;
			@ApiModelProperty(value = "구분")
			private String mberDiv;
			@ApiModelProperty(value = "사업자명/이름")
			private String entrprsNm;
			@ApiModelProperty(value = "신청자 이름")
			private String userNm;
			@ApiModelProperty(value = "직위")
			private String ofcps;
			@ApiModelProperty(value = "연락처")
			private String cttpc;
			@ApiModelProperty(value = "이메일")
			private String email;
			@ApiModelProperty(value = "AI직접단지 참여사업 참여 여부")
			private String partcptnDiv;
		}

		@Data
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		public static class RepairParam  {
			@ApiModelProperty(value = "관리시작일")
			private Date manageBeginDt;
			@ApiModelProperty(value = "관리종료일")
			private Date manageEndDt;
			@ApiModelProperty(value = "관리사유")
			private String manageResn;
			public Date getManageBeginDt() {
				if (this.manageBeginDt != null) {
					return new Date(this.manageBeginDt.getTime());
				}
				return null;
			}
			public void setManageBeginDt(Date manageBeginDt) {
				this.manageBeginDt = null;
				if (manageBeginDt != null) {
					this.manageBeginDt = new Date(manageBeginDt.getTime());
				}
			}
			public Date getManageEndDt() {
				if (this.manageEndDt != null) {
					return new Date(this.manageEndDt.getTime());
				}
				return null;
			}
			public void setManageEndDt(Date manageEndDt) {
				this.manageEndDt = null;
				if (manageEndDt != null) {
					this.manageEndDt = new Date(manageEndDt.getTime());
				}
			}
		}

		@Data
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		public static class CorrectParam {
			@ApiModelProperty(value = "마지막교정일")
			private Date lastCrrcDt;
			@ApiModelProperty(value = "관리시작일")
			private Date manageBeginDt;
			@ApiModelProperty(value = "관리종료일")
			private Date manageEndDt;
			@ApiModelProperty(value = "교정기관")
			private String crrcInstt;
			@ApiModelProperty(value = "관리사유")
			private String manageResn;
			public Date getLastCrrcDt() {
				if (this.lastCrrcDt != null) {
					return new Date(this.lastCrrcDt.getTime());
				}
				return null;
			}
			public void setLastCrrcDt(Date lastCrrcDt) {
				this.lastCrrcDt = null;
				if (lastCrrcDt != null) {
					this.lastCrrcDt = new Date(lastCrrcDt.getTime());
				}
			}
			public Date getManageBeginDt() {
				if (this.manageBeginDt != null) {
					return new Date(this.manageBeginDt.getTime());
				}
				return null;
			}
			public void setManageBeginDt(Date manageBeginDt) {
				this.manageBeginDt = null;
				if (manageBeginDt != null) {
					this.manageBeginDt = new Date(manageBeginDt.getTime());
				}
			}
			public Date getManageEndDt() {
				if (this.manageEndDt != null) {
					return new Date(this.manageEndDt.getTime());
				}
				return null;
			}
			public void setManageEndDt(Date manageEndDt) {
				this.manageEndDt = null;
				if (manageEndDt != null) {
					this.manageEndDt = new Date(manageEndDt.getTime());
				}
			}
		}

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

		TkoutParam tkoutParam;
		RepairParam repairParam;
		CorrectParam correctParam;
		List<MngmDetailParam.EqpmnClParam> eqpmnClParam;
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
