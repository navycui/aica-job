package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.BhExmnt;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrBhExmnt extends BhExmnt implements Serializable {
	private static final long serialVersionUID = 4861474346771765623L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
