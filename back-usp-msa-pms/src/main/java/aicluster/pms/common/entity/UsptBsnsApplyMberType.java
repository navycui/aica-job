package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.ApplyMberType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsApplyMberType extends ApplyMberType implements Serializable {
	private static final long serialVersionUID = 3201479892071068668L;
	private String bsnsCd;			/** 사업코드 */
}
