package aicluster.framework.common.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtSesionInfo;

@Repository("FwCmmtSesionInfoDao")
public interface CmmtSesionInfoDao {
	CmmtSesionInfo select(String sessionId);
	/**
	 * 로그인 연속 실패관리 시에만 사용할 것
	 * @param sessionId
	 * @return
	 */
	CmmtSesionInfo selectById(String sessionId);
	void insert(CmmtSesionInfo session);
	void update(CmmtSesionInfo session);
	void updateExpiredDt(
			@Param("sessionId") String sessionId,
			@Param("expiredDt") Date expiredDt);
	void deleteExpired();
	int delete(String sessionId);
}
