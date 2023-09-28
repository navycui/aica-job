package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FrontMainRecomendParam implements Serializable {
	private static final long serialVersionUID = 1272022393643837026L;
	/** 창업단계 분류코드 */
	private String fntnRecomendClCd;
	/** 사업분야 분류코드 */
	List<String> recomendCl;
}
