package aicluster.framework.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtAuthorRole;

@Repository
public interface FwAuthorRoleDao {
	List<CmmtAuthorRole> selectList(String authorityId);
}
