package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.api.bsnsapply.dto.SamenssRateParam;
import aicluster.pms.common.entity.UsptBsnsPblancApplyDplctAtchmnfl;

@Repository
public interface UsptBsnsPblancApplyDplctAtchmnflDao {
	/** 등록 */
	 int insert(UsptBsnsPblancApplyDplctAtchmnfl info);
	/** 삭제 */
	int delete(UsptBsnsPblancApplyDplctAtchmnfl info);

	/*건수조회*/
	int selectSamenssRateListCnt(SamenssRateParam info);
	/*목록조회*/
	List<UsptBsnsPblancApplyDplctAtchmnfl> selectSamenssRateList(SamenssRateParam info);
}
