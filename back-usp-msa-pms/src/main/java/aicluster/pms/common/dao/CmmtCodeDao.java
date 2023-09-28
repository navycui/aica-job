package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.CmmtCode;

@Repository
public interface CmmtCodeDao {
	CmmtCode select(@Param("codeGroup") String codeGroup, @Param("code") String code);
	List<CmmtCode> selectList(String codeGroup);
	List<CmmtCode> selectList_enabled(@Param("codeGroup") String codeGroup, @Param("codeType") String codeType);
}
