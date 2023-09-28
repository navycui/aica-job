package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvlResutlDspthInfoDto implements Serializable {

	private static final long serialVersionUID = -7616052084795632388L;

	String  evlCmitId;                      /** 평가위원회ID */
	String  dspthTrgetCo;                   /** 통보대상수 */
	String  evlSttusCd;                     /** 평가상태코드(G:EVL_ST) */
	String  evlSttusNm;                     /** 평가상태명 */

    String  sndngMth;						/** 발송방법 */
    String  sndngCn;						/** 발송내용 */
    String  resultMsg;						/** 결과메세지 */
}
