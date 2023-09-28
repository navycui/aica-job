package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtEmailSndngDtls;

@Repository
public interface CmmtEmailSndngDtlsDao {
	void insert(CmmtEmailSndngDtls email);

	void insertList(@Param("list") List<CmmtEmailSndngDtls> list);

	List<CmmtEmailSndngDtls> selectList(
			@Param("beginDay") String beginDay,
			@Param("endDay") String endDay,
			@Param("senderEmail") String senderEmail,
			@Param("senderNm") String senderNm,
			@Param("emailCn") String emailCn);
}
