package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlCharmnOpinionDto implements Serializable {
	private static final long serialVersionUID = 1893163424465551925L;
	/** 평가위원장종합의견내용 */
	private String evlCharmnOpinionCn;
}
