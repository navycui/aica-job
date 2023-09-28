package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FrontBsnsPblancListDto implements Serializable {
	private static final long serialVersionUID = -634314647679460146L;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 접수종료일 */
	private String rceptEndde;
	/** 공고일 */
	private String pblancDay;
	/** 접수상태 */
	private String pblancSttus;
	/** 접수기간 */
	private String rceptPd;
	/** 공고요약 */
	private String pblancSumry;
	/** 조회수 */
	private Integer rdcnt;
	/** 마감남은일 */
	private Integer rmndrDay;
	/** 사업분야 */
	private String recomendCl;
	/** 신규여부 */
	private String isNew;

	/** 이전공고ID */
	@JsonIgnore
	private String prePblancId;
	/** 이전공고명 */
	@JsonIgnore
	private String prePblancNm;
	/** 다음공고ID */
	@JsonIgnore
	private String nextPblancId;
	/** 다음공고명 */
	@JsonIgnore
	private String nextPblancNm;
}
