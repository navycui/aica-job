package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.Chklst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrChklst extends Chklst implements Serializable {
	private static final long serialVersionUID = 1911969258638202143L;
	private String stdrBsnsCd;		/** 기준사업코드 */
}
