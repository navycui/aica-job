package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.common.dto.MemberDto;
import aicluster.member.common.dto.MemberSelfDto;
import aicluster.member.common.dto.MemberStatsDto;
import aicluster.member.common.dto.MemberStatsListItemDto;
import aicluster.member.common.entity.CmmtMberInfo;

@Repository
public interface CmmtMberInfoDao {
	CmmtMberInfo select(String memberId);
	CmmtMberInfo selectByLoginId(String loginId);
	CmmtMberInfo selectByNaverToken(String naverToken);
	CmmtMberInfo selectByGoogleToken(String googleToken);
	CmmtMberInfo selectByKakaoToken(String kakaoToken);
	CmmtMberInfo selectByRefreshToken(String refreshToken);
	long selectCountByAuthorityId(String authorityId);
	long selectCount(
			@Param("param") MemberGetListParam param);
	List<MemberDto> selectList(
			@Param("param") MemberGetListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage);
	void insert(CmmtMberInfo member);
	void update(CmmtMberInfo member);
	MemberStatsDto selectCurrStats();
	List<MemberStatsListItemDto> selectDayStatsList(
			@Param("memberType") String memberType,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);
	List<MemberStatsListItemDto> selectMonthStatsList(
			@Param("memberType") String memberType,
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay);
	List<CmmtMberInfo> selectByMemberNm(String memberNm);
	CmmtMberInfo selectByCi(String ci);
	MemberSelfDto selectMe(String memberId);
}
