package aicluster.framework.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.BnMember;

@Repository
public interface FwMberInfoDao {
	BnMember selectBnMember_loginId(String loginId);
	BnMember selectBnMember_refreshToken(String refreshToken);
	BnMember selectBnMember_naverToken(String naverToken);
	void updateRefreshToken(
			@Param("memberId") String memberId,
			@Param("refreshToken") String refreshToken);
}
