package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtCode;

@Repository
public interface CmmtCodeDao {
	void insert(CmmtCode cmmtCode);
	CmmtCode select(
			@Param("codeGroup") String codeGroup,
			@Param("code") String code);
	List<CmmtCode> selectList(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);
	List<CmmtCode> selectList_enabled(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);
	void update(CmmtCode cmmtCode);
	void delete(
			@Param("codeGroup") String codeGroup,
			@Param("code") String code);
}
