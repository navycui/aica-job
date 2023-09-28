package aicluster.member.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtLoginAtpt;

@Repository
public interface CmmtLoginAtptDao {
	void save(
			@Param("memberId") String memberId,
			@Param("memberIp") String memberIp);
	CmmtLoginAtpt select(
			@Param("memberId") String memberId,
			@Param("memberIp") String memberIp);
	void delete(
			@Param("memberId") String memberId,
			@Param("memberIp") String memberIp);
	void deleteOld(
			@Param("memberId") String memberId,
			@Param("memberIp") String memberIp);
}
