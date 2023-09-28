package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontSlctnPblancSlctnDto implements Serializable {
	private static final long serialVersionUID = 6352017539359805863L;
	/** 접수번호 */
	private String receiptNo;
	/** 순번 */
	private int rn;
}
