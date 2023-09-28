package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.api.slctnObjc.dto.FrontSlctnObjcReqstListParam;
import aicluster.pms.api.slctnObjc.dto.SlctnObjcReqstListParam;
import aicluster.pms.common.dto.FrontSlctnObjcDto;
import aicluster.pms.common.dto.FrontSlctnObjcReqstListDto;
import aicluster.pms.common.dto.SlctnObjcDto;
import aicluster.pms.common.dto.SlctnObjcReqstListDto;
import aicluster.pms.common.entity.UsptEvlSlctnObjcReqst;

@Repository
public interface UsptEvlSlctnObjcReqstDao {
	void insert(UsptEvlSlctnObjcReqst info);
	int update(UsptEvlSlctnObjcReqst info);
	UsptEvlSlctnObjcReqst select(String evlSlctnObjcReqstId);
	Long selectCountByTrgetId(String trgetId);

	Long selectListCount(SlctnObjcReqstListParam param);
	List<SlctnObjcReqstListDto> selectList(SlctnObjcReqstListParam param);
	SlctnObjcDto selectDetail(@Param("objcReqstId") String objcReqstId, @Param("insiderId") String insiderId);

	Long selectFrontListCount(FrontSlctnObjcReqstListParam param);
	List<FrontSlctnObjcReqstListDto> selectFrontList(FrontSlctnObjcReqstListParam param);
	FrontSlctnObjcDto selectFrontDetail(@Param("objcReqstId") String objcReqstId, @Param("memberId") String memberId);
}
