package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptLastSlctnObjcReqst;

@Repository
public interface UsptLastSlctnObjcReqstDao {
	void insert(UsptLastSlctnObjcReqst info);
	int update(UsptLastSlctnObjcReqst info);
	UsptLastSlctnObjcReqst select(String lastSlctnObjcReqstId);
	Long selectCountByTrgetId(String trgetId);
}
