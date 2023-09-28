package aicluster.member.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmvUser;

@Repository
public interface CmmvUserDao {
	CmmvUser select( String memberId );
	CmmvUser selectByRefreshToken( String refreshToken );
}
