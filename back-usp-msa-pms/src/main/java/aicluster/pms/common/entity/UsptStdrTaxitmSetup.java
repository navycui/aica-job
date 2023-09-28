package aicluster.pms.common.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrTaxitmSetup extends UsptWctTaxitm implements Serializable {
	private static final long serialVersionUID = 11955855927036110L;
	private String stdrBsnsCd;		/** 기준사업코드 */
}
