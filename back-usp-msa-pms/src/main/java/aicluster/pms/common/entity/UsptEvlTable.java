package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlTable implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4234458826286093692L;

	/**  순번*/
	private int rn;

	/** 평가표ID */
	private String  evlTableId;
	/** 평가표명 */
	private String  evlTableNm;
	/** 평가방식코드(G:EVL_MTHD) */
	private String  evlMthdCd;
	/** 개별의견 작성여부 */
	private Boolean indvdlzOpinionWritng;
	/** 사용여부 */
	private Boolean enabled;
	/** 평가방식명(G:EVL_MTHD) */
	private String  evlMthdNm;
	/** 개별의견 작성여부명 */
	private String indvdlzOpinionWritngNm;
	/** 사용여부명 */
	private String enabledNm;
	/** 선택여부 */
	private boolean isCheck;

	/** 등록일 */
	private String rgsde;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;


	/** 일반 */
	List<UsptEvlIem> usptEvlIemList;

	/** 가점 */
	List<UsptEvlIem> usptEvlAddIemList;

	/** 감점 */
	List<UsptEvlIem> usptEvlMinusIemList;

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
         }
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
