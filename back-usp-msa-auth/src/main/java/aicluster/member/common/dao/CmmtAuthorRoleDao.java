package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtAuthorRole;

@Repository
public interface CmmtAuthorRoleDao {
	List<CmmtAuthorRole> selectList(String authorityId);
	void insert( CmmtAuthorRole role );
	void insertList(@Param("list") List<CmmtAuthorRole> list);
	List<CmmtAuthorRole> selectByRoleId(String roleId);
	void deleteAuthorityId(String authorityId);
}
