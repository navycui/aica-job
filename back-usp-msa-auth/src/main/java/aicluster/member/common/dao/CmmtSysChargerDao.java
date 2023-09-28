package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtSysCharger;

@Repository
public interface CmmtSysChargerDao {
	List<CmmtSysCharger> selectList();
	void insertList(@Param("list") List<CmmtSysCharger> list);
	void deleteList();
}
