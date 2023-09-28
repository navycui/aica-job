package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.BhExmnt;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsBhExmnt extends BhExmnt implements Serializable {
	private static final long serialVersionUID = -4572910007244758697L;
	private String bsnsCd;			/** 사업코드 */
}
