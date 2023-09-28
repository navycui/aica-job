package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.TermsListItem;
import aicluster.common.common.entity.CmmtStplat;

@Repository
public interface CmmtStplatDao {
	void insert(CmmtStplat cmmtTerms);

	CmmtStplat select(
		@Param("termsType") String termsType,
		@Param("beginDay") String beginDay,
		@Param("required") boolean required);

	List<CmmtStplat> selectSet(
		@Param("termsType") String termsType,
		@Param("beginDay") String beginDay);

	List<TermsListItem> selectList_beginDay(
		@Param("termsType") String termsType,
		@Param("srchCd") String srchCd);

	void update(CmmtStplat cmmtTerms);

	void delete(
		@Param("termsType") String termsType,
		@Param("beginDay") String beginDay);

	List<CmmtStplat> select_today(String termsType);
}
