package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdx;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsRsltIdx extends RsltIdx implements Serializable {
	private static final long serialVersionUID = 4995376228209285051L;
	private String bsnsCd;		/** 사업코드 */
}
