package aicluster.pms.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import bnet.library.util.CoreUtils.date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FrontReprtDto extends FrontReprtListDto {
	private static final long serialVersionUID = 8406817083006659452L;
	private String reprtSumryCn;
	/** 첨부파일그룹ID */
	@JsonIgnore
	private String attachmentGroupId;

	public String getPresentnDate() {
		if(this.getPresentnDt() == null) {
			return "";
		}
		return date.format(this.getPresentnDt(), "yyyy-MM-dd");
	}

	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachFileList;
}
