package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtSmsSndngDtls;

@Repository
public interface CmmtSmsSndngDtlsDao {
	void insert(CmmtSmsSndngDtls sms);

	void insertList(@Param("list") List<CmmtSmsSndngDtls> list);

	List<CmmtSmsSndngDtls> selectList(
				@Param("beginDay") String beginDay,
				@Param("endDay") String endDay,
				@Param("senderMobileNo") String senderMobileNo,
				@Param("smsCn") String smsCn);
}
