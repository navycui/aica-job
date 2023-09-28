package aicluster.framework.common.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2651958862464209840L;
	private String codeGroup;		/** 코드그 */
	private String code;			/** 코드 */
	private String codeNm;			/** 코드명 */
	private String remark;			/** 비고 */
	private String codeType;		/** 코드구분 */
	private Boolean enabled;		/** 사용여부 */
	private Long sortOrder;			/** 정렬순서 */
}
