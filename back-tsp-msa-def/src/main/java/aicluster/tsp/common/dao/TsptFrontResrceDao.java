package aicluster.tsp.common.dao;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.front.usereqst.resrce.param.UseReqstResrceParam;
import aicluster.tsp.common.entity.TsptApplcnt;
import aicluster.tsp.common.entity.TsptResrceUseReqst;
import org.springframework.stereotype.Repository;

@Repository
public interface TsptFrontResrceDao {

    // 신청자 정보 조회
    void insertUseReqstResrce(TsptResrceUseReqst param);

    // 신청자 정보 업데이트
    void upsertUserInfo(UseReqstResrceParam param);
}
