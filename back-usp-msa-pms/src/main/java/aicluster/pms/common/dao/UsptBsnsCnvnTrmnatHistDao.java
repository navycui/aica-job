package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnTrmnatHist;

@Repository
public interface UsptBsnsCnvnTrmnatHistDao {
	/*
	 * 사업협약해지이력
	 *
	 */

	/**조회 */
	List<UsptBsnsCnvnTrmnatHist> selectList(String  bsnsCnvnId);
	/**이력등록 */
	int insert(UsptBsnsCnvnTrmnatHist usptBsnsCnvnTrmnatHist);

}
