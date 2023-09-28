package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CodeDto;

@Repository
public interface FwCodeDao {
	CodeDto select(
			@Param("codeGroup") String codeGroup,
			@Param("code") String code);
	List<CodeDto> selectList(
			@Param("codeGroup") String codeGroup);
	List<CodeDto> selectList_codeType(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);
	List<CodeDto> selectList_enabled(
			@Param("codeGroup") String codeGroup);
	List<CodeDto> selectList_codeType_enabled(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);
}
