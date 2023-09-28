package aicluster.pms.common.entity;

import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptEvlSlctnObjcReqst extends SlctnObjcReqst {
	private static final long serialVersionUID = 6018823197536868490L;
	/** 평가선정이의신청ID */
	private String evlSlctnObjcReqstId;
	/** 평가대상ID */
	private String evlTrgetId;
}
