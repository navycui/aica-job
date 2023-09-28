package aicluster.pms.common.entity;

import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptLastSlctnObjcReqst extends SlctnObjcReqst {
	private static final long serialVersionUID = 4078097171525424925L;
	/** 최종선정이의신청ID */
	private String lastSlctnObjcReqstId;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
}
