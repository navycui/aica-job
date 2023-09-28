package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.Chklst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsPblancApplyChklst extends Chklst implements Serializable {
	private static final long serialVersionUID = 3905604390784544169L;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 체크결과구분코드 */
	private String ceckResultDivCd;
}