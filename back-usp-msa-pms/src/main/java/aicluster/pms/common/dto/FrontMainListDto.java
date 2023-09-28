package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontMainListDto implements Serializable {
	private static final long serialVersionUID = -6325850863062715899L;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 접수기간 */
	private String rceptPd;
	/** 마감남은일 */
	private Integer rmndrDay;
	/** 사업분야 */
	private String recomendCl;
	/** 순번 */
	private Long rn;
}
