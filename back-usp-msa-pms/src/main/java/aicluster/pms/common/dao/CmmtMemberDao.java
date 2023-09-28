package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.BsnsPortListDto;
import aicluster.pms.common.dto.CmmtMemberBizDto;
import aicluster.pms.common.dto.EntrprsSttusListDto;
import aicluster.pms.common.entity.CmmtMember;

@Repository
public interface CmmtMemberDao {
	CmmtMember select(String memberId);
	/**
	 * 기업현황
	 * @param bsnsYear
	 * @return
	 */
	List<EntrprsSttusListDto> selectEntrprsSttusList(String bsnsYear);
	/**
	 * 사업지원현황
	 * @param bsnsYear
	 * @return
	 */
	List<BsnsPortListDto> selectBsnsPortSttusList(String bsnsYear);

	/**
	 * 기업검색
	 * @param memberNm
	 * @return
	 */
	List<CmmtMemberBizDto> selectBizList(CmmtMemberBizDto cmmtMemberBizDto);

	/**
	 * 기업검색 총건수
	 * @param memberNm
	 * @return
	 */
	Long selectBizListTotCnt(String memberNm);

	/**
	 * 회원의 직원의 loginID, 이름 조회
	 * @param memberId
	 * @return
	 */
	CmmtMember selectLoginIdNm(String memberId);
}
