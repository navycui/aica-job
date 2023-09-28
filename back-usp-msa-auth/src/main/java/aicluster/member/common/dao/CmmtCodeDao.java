package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtCode;

@Repository
public interface CmmtCodeDao {
	void insert(CmmtCode cmmtCode);
	CmmtCode select(
			@Param("codeGroup") String codeGroup,
			@Param("code") String code);
	List<CmmtCode> selectList(String codeGroup);
	List<CmmtCode> selectList_enabled(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);
	void delete(
			@Param("codeGroup") String codeGroup,
			@Param("code") String code);
	void update(CmmtCode cmmtCode);
}
