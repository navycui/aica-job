package aicluster.framework.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.EvlMfcmm;

@Repository
public interface FwUsptEvlMfcmmDao {
	EvlMfcmm selectByExpertId(
			@Param("evlCmitId") String evlCmitId,
			@Param("expertId") String expertId);

	BnMember selectBnMember(String evlMfcmmId);

	EvlMfcmm select(String evlMfcmmId);

	BnMember selectBnMember_refreshToken(String refreshToken);

	void updateRefreshToken(String memberId, String refreshToken);
}
