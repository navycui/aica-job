package aicluster.member.api.member.dto;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseParam {

	private String  memberId;			     /** 회원ID */
	private String  cmpnyTypeCd;			     /** 기업유형코드(G:CMPNY_TYPE)        */
	private String  industRealmCd;			     /** 산업분야코드(G:INDUST_REALM)      */
	private String  fondDay;				     /** 설립일                            */
	private Long    emplyCnt;			     /** 종업원수                          */
	private Long    resdngNmpr;			     /** 상주인원수                        */
	private Long    empmnPrearngeNmpr;		     /** 채용예정인력수                    */
	private String  induty;				     /** 업종                              */
	private String  mainInduty;			     /** 주업종                            */
	private String  mainTchnlgyProduct;		     /** 주요기술 및 생산품                */
	private String  zip;				     /** 우편번호                          */
	private String  adres;				     /** 주소                              */
	private String  fxnum;				     /** 팩스번호                          */
	private String  reprsntTelno;			     /** 대표 전화번호                     */
	private String  ceoTelno;			     /** 대표자 전화번호                   */
	private String  ceoEmail;			     /** 대표자 이메일                     */
	private String  newFntnPlanCd;			     /** 신규창업계획코드(G:NEW_FNTN_PLAN) */
	private String  fondPlanCd;			     /** 이전 및 설립계획코드(G:FOND_PLAN) */
	private Long    prvyySalamt;             /** 전년도매출금액 */

	public String getEncFxnum() {
		if (string.isBlank(this.fxnum)) {
			return null;
		}
		return aes256.encrypt(this.fxnum, this.memberId);
	}
	public String getEncReprsntTelno() {
		if (string.isBlank(this.reprsntTelno)) {
			return null;
		}
		return aes256.encrypt(this.reprsntTelno, this.memberId);
	}
	public String getEncCeoTelno() {
		if (string.isBlank(this.ceoTelno)) {
			return null;
		}
		return aes256.encrypt(this.ceoTelno, this.memberId);
	}
	public String getEncCeoEmail() {
		if (string.isBlank(this.ceoEmail)) {
			return null;
		}
		return aes256.encrypt(this.ceoEmail, this.memberId);
	}
}
