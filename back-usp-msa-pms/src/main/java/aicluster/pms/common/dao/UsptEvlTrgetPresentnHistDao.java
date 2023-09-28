package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlTrgetPresentnHist;

@Repository
public interface UsptEvlTrgetPresentnHistDao {

	//처리이력등록
	int insert(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist);

	//이력 조회
	List<UsptEvlTrgetPresentnHist> selectHistList(String evlTrgetId);

	UsptEvlTrgetPresentnHist select(UsptEvlTrgetPresentnHist usptEvlTrgetPresentnHist);

	UsptEvlTrgetPresentnHist selectReason(String evlTrgetId);

}
