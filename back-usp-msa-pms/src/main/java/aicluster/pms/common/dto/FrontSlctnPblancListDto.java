package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FrontSlctnPblancListDto implements Serializable {
	private static final long serialVersionUID = -6627930573963001731L;
	/** 선정공고ID */
	private String slctnPblancId;
	/** 공고명 */
	private String slctnPblancNm;
	/** 선정공고요약 */
	private String slctnPblancSumry;
	/** 공고명 */
	private String pblancNm;
	/** 신규여부 */
	private String isNew;

	/** 이전선정공고ID */
	@JsonIgnore
	private String preSlctnPblancId;
	/** 이전선정공고명 */
	@JsonIgnore
	private String preSlctnPblancNm;
	/** 다음선정공고ID */
	@JsonIgnore
	private String nextSlctnPblancId;
	/** 다음선정공고명 */
	@JsonIgnore
	private String nextSlctnPblancNm;
}
