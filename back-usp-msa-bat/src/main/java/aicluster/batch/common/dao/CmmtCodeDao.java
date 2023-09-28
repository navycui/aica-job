package aicluster.batch.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.CmmtCode;

@Repository
public interface CmmtCodeDao {
	List<CmmtCode> selectList_enabled(
			@Param("codeGroup") String codeGroup,
			@Param("codeType") String codeType);

	void insert(CmmtCode info);
}
