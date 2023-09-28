package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlTableSearchParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8493920028206169468L;

	/** 평가방식코드(G:EVL_MTHD) */
	private String  evlMthdCd;
	/** 사용여부 */
	private Boolean enabled;
	/** 평가표명 */
	private String  evlTableNm;

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

}
