package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.ApplyAttachDto;
import aicluster.pms.common.dto.ApplyBhExmntDto;
import lombok.Data;

@Data
public class BsnsApplyBhExmntDto implements Serializable {
	private static final long serialVersionUID = -649045356990975285L;
	/** 보완의견내용 */
	private String makeupOpinionCn;
	/** 사전검토 목록 */
	List<ApplyBhExmntDto> bhExmntList;
	/** 첨부파일 목록 */
	List<ApplyAttachDto> atchmnflList;
}
