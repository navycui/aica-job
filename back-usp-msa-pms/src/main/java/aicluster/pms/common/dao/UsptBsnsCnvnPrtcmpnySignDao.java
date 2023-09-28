package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;

@Repository
public interface UsptBsnsCnvnPrtcmpnySignDao {
	/*
	 * 사업협약참여기업서명
	 *
	 */
	//등록
	int insert(UsptBsnsCnvnPrtcmpnySign usptBsnsCnvnPrtcmpnySign);
	//조회
	List<UsptBsnsCnvnPrtcmpnySign> selectList(String bsnsCnvnId);
	// 사업협약참여기업서명 대상 여부 조회
	UsptBsnsCnvnPrtcmpnySign selectSignRegYn(@Param("bsnsCnvnId") String bsnsCnvnId, @Param("memberId") String memberId);
	//서명일자 변경
	int updateSign(UsptBsnsCnvnPrtcmpnySign usptBsnsCnvnPrtcmpnySign);
	//사업협약ID의 서명할 전체 건수 조회
	int selectSignTotCnt(String bsnsCnvnId);
	//사업협약ID의 서명한 건수 조회
	int selectSignCnt(String bsnsCnvnId);

	int delete(String bsnsCnvnId);

}
