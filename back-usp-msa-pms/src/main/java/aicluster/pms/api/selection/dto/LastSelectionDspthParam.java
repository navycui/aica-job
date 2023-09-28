package aicluster.pms.api.selection.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LastSelectionDspthParam implements Serializable {
	private static final long serialVersionUID = -7597098700864461463L;
	/** 발송대상
	 * ( slctn : 선정, ptrmsn : 탈락 )
	 */
	private String sndngTrget;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
}
