package aicluster.member.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtAuthor;
import aicluster.member.common.entity.CmmtAuthorRole;

@Repository
public interface CmmtAuthorDao {
	List<CmmtAuthor> selectAll();
	List<CmmtAuthorRole> selectByRoleId(String roleId);
	CmmtAuthor select(String authorityId);
	CmmtAuthor selectByName(String authorityNm);
	void insert(CmmtAuthor authority);
	void update(CmmtAuthor authority);
	void delete(String authorityId);
}
