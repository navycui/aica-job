package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CmmtMemberBizDto implements Serializable {

	/**
	 * 기업고객
	 */
	private static final long serialVersionUID = 8124642761548620970L;
	/**회원ID*/
	String memberId;
	/**회원명(사업자명)*/
	String memberNm;
	/**대표자명*/
	String ceoNm;
	/**사업자번호(기업회원)*/
	String bizrno;

	Long beginRowNum;
	Long itemsPerPage;
}
