package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AlrsrcFirstDstbDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2666318923732095475L;

	private String alrsrcId		;		/** 자원할당ID     */
	private String rsrcId		;		/** 자원ID         */
	private String rsrcGroupCd	;		/** 자원그룹코드   */
	private String rsrcTypeNm	;		/** 자원유형명     */
	private String rsrcTypeUnitCd;      /** 자원유형단위코드 */
	private String rsrcTypeUnitNm;      /** 자원유형단위명 */
	private boolean rsrcUseYn	;		/** 자원사용여부   */
	private int rsrcDstbQy		;		/** 자원배분량     */
	private String rsrcDstbCn	;		/** 자원배분내용   */
}
