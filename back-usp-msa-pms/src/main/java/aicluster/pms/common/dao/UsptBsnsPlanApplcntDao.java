package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPlanApplcnt;

@Repository
public interface UsptBsnsPlanApplcntDao {

	int delete(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);

	int insert(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);

	int update(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);

	List<UsptBsnsPlanApplcnt> selectList(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);

	UsptBsnsPlanApplcnt selectOne(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);
	/*협약변경 승인 등록*/
	int updateChangeCnvn(UsptBsnsPlanApplcnt usptBsnsPlanApplcnt);



}
