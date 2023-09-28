package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptWctTaxitm;

@Repository
public interface UsptWctTaxitmDao {
	void insert(UsptWctTaxitm info);
	int update(UsptWctTaxitm info);
	int delete(UsptWctTaxitm info);
	int deleteAll(String wctIoeId);

	/**
	 * 사용 건수 조회
	 * @param info
	 * @return
	 */
	int checkIsUse(UsptWctTaxitm info);
}
