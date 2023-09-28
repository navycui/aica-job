package aicluster.pms.api.cnvncncls.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class FrontCnvnCnclsParam implements Serializable{
	/**
	 *전자협약 관리_front
	 */
	private static final long serialVersionUID = 5446810311626830228L;
	/**협약일자시작**/
	String cnvnDeStart;
	/**협약일자종료**/
	String cnvnDeEnd;
	/** 협약상태코드(G:CNVN_STTUS) */
	String  cnvnSttusCd;
	/** 과제명(국문) */
	String taskNmKo;
	/**사업명*/
	String bsnsNm;
	/** 공고명 */
	String pblancNm;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 신청ID */
	String  applyId;
	/** 신청자ID */
	String memberId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
