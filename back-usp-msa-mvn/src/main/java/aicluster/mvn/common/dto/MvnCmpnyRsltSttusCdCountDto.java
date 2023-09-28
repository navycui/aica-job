package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnCmpnyRsltSttusCdCountDto implements Serializable {

	private static final long serialVersionUID = 5010902987383316541L;

	private Long prCount;	/** 제출요청 */
	private Long psCount;	/** 제출     */
	private Long srCount;	/** 보완요청 */
	private Long rcCount;	/** 접수완료 */
}
