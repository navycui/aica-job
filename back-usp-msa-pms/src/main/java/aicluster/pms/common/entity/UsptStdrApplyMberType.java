package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.ApplyMberType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrApplyMberType extends ApplyMberType implements Serializable {
	private static final long serialVersionUID = -6751760002559952698L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
