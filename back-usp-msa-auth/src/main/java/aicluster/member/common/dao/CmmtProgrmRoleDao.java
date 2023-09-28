package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtProgrmRole;

@Repository
public interface CmmtProgrmRoleDao {
	List<CmmtProgrmRole> selectList(String programId);
	List<CmmtProgrmRole> selectByRoleId(String roleId);
	void insertList(@Param("list") List<CmmtProgrmRole> list);
	void deleteProgramId(String programId);
}
