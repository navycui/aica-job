package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertClCharger;

@Repository
public interface UsptExpertClChargerDao {

	/*나의 담당 분류*/
	List<UsptExpertClCharger> selectListMyExpertCl(String memberId);
	/* 담당자 List 조회*/
	List<UsptExpertClCharger> selectList(String expertClId);

	int insert(UsptExpertClCharger usptExpertClCharger);
	int update(UsptExpertClCharger usptExpertClCharger);
	int delete(@Param("expertClId") String expertClId , @Param("memberId") String memberId);
}
