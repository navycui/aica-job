package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptBsnsPblancApplyBhExmnt;
import lombok.Data;

@Data
public class BsnsApplyBhExmntParam implements Serializable {
	private static final long serialVersionUID = -8380718634626609412L;
	/** 보완의견내용 */
	private String makeupOpinionCn;
	/** 사전검토 목록 */
	List<UsptBsnsPblancApplyBhExmnt> bhExmntList;
}
