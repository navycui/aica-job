package aicluster.pms.common.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrIoeSetup extends UsptWctIoe implements Serializable {
	private static final long serialVersionUID = -8565519317734881083L;
	private String stdrBsnsCd;		/** 기준사업코드 */
}
