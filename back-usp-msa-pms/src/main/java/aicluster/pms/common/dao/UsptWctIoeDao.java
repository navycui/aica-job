package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.WctIoeDto;
import aicluster.pms.common.entity.UsptWctIoe;

@Repository
public interface UsptWctIoeDao {
	void insert(UsptWctIoe info);
	int update(UsptWctIoe info);
	int delete(UsptWctIoe info);
	List<WctIoeDto> selectList();

	/**
	 * 사용 건수 조회
	 * @param info
	 * @return
	 */
	int checkIsUse(UsptWctIoe info);



}
