package aicluster.pms.common.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsTaxitmSetup extends UsptWctTaxitm implements Serializable {
	private static final long serialVersionUID = -5923032666737488791L;
	private String bsnsCd;		/** 사업코드 */
}
