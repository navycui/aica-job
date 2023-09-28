package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtCodeGroup;

@Repository
public interface CmmtCodeGroupDao {
	List<CmmtCodeGroup> selectList(
			@Param("codeGroup") String codeGroup,
			@Param("codeGroupNm") String codeGroupNm);
	CmmtCodeGroup select(String codeGroup);
	void insert(CmmtCodeGroup codeGroup);
	void update(CmmtCodeGroup codeGroup);
	void delete(String codeGroup);
}
