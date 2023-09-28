package aicluster.pms.common.dto;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.AtchmnflSetup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApplyAttachDto extends AtchmnflSetup implements Serializable {
	private static final long serialVersionUID = -1022532011440475423L;
	/** 첨부파일ID */
	private String attachmentId;
	/** 첨부파일명 */
	private String attachmentNm;
	/** 첨부파일크기 */
	private Long attachmentSize;
	/** 동일비율 */
	private Float samenssRate;

}
