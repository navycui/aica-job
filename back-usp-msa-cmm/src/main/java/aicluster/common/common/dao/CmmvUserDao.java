package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmvUser;

@Repository
public interface CmmvUserDao {

	CmmvUser select(String memberId);
	CmmvUser selectByRefreshToken( String refreshToken );

}
