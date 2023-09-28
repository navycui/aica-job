package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtStplatAgreDtls;

@Repository
public interface FwStplatAgreDtlsDao {
	void insertList(List<CmmtStplatAgreDtls> list);
	void delete(
			@Param("termsType") String termsType,
			@Param("memberId") String memberId);
	List<CmmtStplatAgreDtls> selectList(
			@Param("termsType") String termsType,
			@Param("beginDay") String beginDay,
			@Param("required") Boolean required,
			@Param("memberId") String memberId);
	List<CmmtStplatAgreDtls> selectExpiredList(
			@Param("expiredCalType") String expiredCalType,
			@Param("expiredCalVal") int expiredCalVal,
			@Param("termsType") String termsType,
			@Param("beginDay") String beginDay,
			@Param("required") Boolean required);
}
