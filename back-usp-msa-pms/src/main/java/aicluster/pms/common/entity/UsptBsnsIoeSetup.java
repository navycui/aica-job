package aicluster.pms.common.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsIoeSetup extends UsptWctIoe implements Serializable {
	private static final long serialVersionUID = 8364545515018702749L;
	private String bsnsCd;		/** 사업코드 */
}
