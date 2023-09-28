package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.Chklst;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsChklst extends Chklst implements Serializable {
	private static final long serialVersionUID = -2442527217014362818L;
	@JsonIgnore
	private String bsnsCd;		/** 사업코드 */
}
