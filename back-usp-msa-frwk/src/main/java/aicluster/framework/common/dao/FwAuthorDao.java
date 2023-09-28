package aicluster.framework.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.BnAuthorityRole;
import aicluster.framework.common.entity.ProgramRole;

@Repository
public interface FwAuthorDao {
	List<BnAuthorityRole> selectAuthorityRoles(String authorityId);

	List<ProgramRole> selectProgramRoles();
}
