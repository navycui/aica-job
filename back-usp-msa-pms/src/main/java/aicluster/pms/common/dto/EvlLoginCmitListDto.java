package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvlLoginCmitListDto implements Serializable {

	private static final long serialVersionUID = 5234693517004289633L;

	String  evlCmitId;                      /** 평가위원회ID */
	String  evlCmitNm;                      /** 평가위원회명 */

}
