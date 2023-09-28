package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCnsltHist;

@Repository
public interface UsptCnsltHistDao {

	//상담이력 등록
	void insert(UsptCnsltHist usptCnsltHist);

	//상담이력 조회
	UsptCnsltHist select(String histId);

	//상담이력 리스트
	List<UsptCnsltHist> selectList(String extrcMfcmmId);

	int delete(UsptCnsltHist usptCnsltHist);

	//상담이력 삭제 by 평가위원회
	void deleteByCmitId(String evlCmitId);
}
