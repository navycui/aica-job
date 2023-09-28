package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.CnvnChangeChangeIemDivDto;
import aicluster.pms.common.dto.CnvnChangeDto;
import lombok.Data;

@Data
public class CnvnChangeHistBaseInfoDto implements Serializable{
	/**
	 * 협약변경내역 기본정보
	 */
	private static final long serialVersionUID = -8819465424762745219L;

	/** 협약변경내역 기본정보*/
	private CnvnChangeDto cnvnChangeHistBaseInfo;
	/** 협약변경항목 List*/
	List<CnvnChangeChangeIemDivDto> cnvnChangeChangeIemDivList;

}
