package aicluster.pms.common.dto;

import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FrontSlctnObjcDto extends SlctnObjcReqst {
	private static final long serialVersionUID = -815386832973370840L;
	/** 이의신청ID */
	private String objcReqstId;
	/** 평가단계명 */
	private String evlStepNm;
	/** 과제명 */
	private String taskNm;
	/** 공고명 */
	private String pblancNm;
	/** 접수번호 */
	private String receiptNo;
	/** 신청자 첨부파일 */
	List<CmmtAtchmnfl> applcntAttachmentFileList;
	/** 심의결과 첨부파일 */
	List<CmmtAtchmnfl> dlbrtAttachmentFileList;

}
