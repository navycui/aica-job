package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlResultDspthParam implements Serializable {
	private static final long serialVersionUID = -8616905971164544957L;
	/** 발송대상
	 *  ( SLCTN : 선정 , PTRMSN : 탈락 )
	 */
	private String sndngTrget;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
}
