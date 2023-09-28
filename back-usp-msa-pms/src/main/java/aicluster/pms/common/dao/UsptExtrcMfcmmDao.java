package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExtrcMfcmm;

@Repository
public interface UsptExtrcMfcmmDao {

	//추출위원 등록
	void insertList(@Param("list") List<UsptExtrcMfcmm> usptExtrcMfcmmList);

	//섭외결과 업데이트
	void update(UsptExtrcMfcmm usptExtrcMfcmm);

	void deleteByCmitId(String EvlCmitId);

	UsptExtrcMfcmm select(String extrcMfcmmId);

	int selectCntExtrcExpert(String expertId);
}
