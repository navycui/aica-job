package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;

@Repository
public interface UsptBsnsPblancApplyTaskDao {

	void insert(UsptBsnsPblancApplyTask info);
	int update(UsptBsnsPblancApplyTask info);
	UsptBsnsPblancApplyTask select(String applyId);

	/** 최근발송일 저장 */
	int updateRecentSendDate(UsptBsnsPblancApplyTask info);

}
