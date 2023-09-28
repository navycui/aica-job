package aicluster.batch.api.mvn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.common.dao.UsptMvnChcktHistDao;
import aicluster.batch.common.dao.UsptMvnChcktReqstDao;
import aicluster.batch.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.batch.common.dao.UsptMvnFcltyInfoDao;
import aicluster.batch.common.entity.UsptMvnChcktHist;
import aicluster.batch.common.entity.UsptMvnChcktReqst;
import aicluster.batch.common.entity.UsptMvnEntrpsInfo;
import aicluster.batch.common.entity.UsptMvnFcltyInfo;
import aicluster.batch.common.util.CodeExt;
import bnet.library.util.CoreUtils.string;

@Service
public class MvnCheckoutService {

	@Autowired
	private UsptMvnChcktReqstDao checkoutDao;

	@Autowired
	private UsptMvnChcktHistDao checkoutHistDao;

	@Autowired
	private UsptMvnEntrpsInfoDao mvnCmpnyDao;

	@Autowired
	private UsptMvnFcltyInfoDao mvnFcDao;

	/**
	 * 퇴실신청 승인된 입주업체 퇴실처리
	 * (퇴실처리 이메일 발송)
	 *
	 * @return 퇴실처리 건수
	 */
	public int updateCheckout()
	{
		Date now = new Date();
		String workerId = "BATCH";
		int checkoutPrcsCnt = 0;

		// 퇴실 대상 데이터 조회(퇴실신청이 '승인' 상태이고 입주업체퇴실여부가 'false'이며 퇴실예정일이 경과한 데이터)
		List<UsptMvnChcktReqst> checkoutList = checkoutDao.selectList_mvnCheckoutYn();

		List<UsptMvnChcktHist> histList = new ArrayList<>();
		for (UsptMvnChcktReqst checkoutReq : checkoutList) {
    		UsptMvnEntrpsInfo mvnCompany = mvnCmpnyDao.select(checkoutReq.getMvnId());
    		UsptMvnFcltyInfo mvnFc = mvnFcDao.select(mvnCompany.getMvnFcId());

    		// 입주업체를 '입주종료'로 변경한다.
            mvnCompany.setMvnCmpnySt(CodeExt.mvnCmpnySt.입주종료);
            mvnCompany.setMvnCmpnyStDt(now);
            mvnCompany.setUpdaterId(workerId);
            mvnCompany.setUpdatedDt(now);
            mvnCmpnyDao.update(mvnCompany);

			// 입주시설을 '공실'로 변경한다.
            mvnFc.setMvnSt(CodeExt.mvnSt.공실);
            mvnFc.setMvnStDt(now);
            mvnFc.setCurOccupantId(null);
            mvnFc.setUpdaterId(workerId);
            mvnFc.setUpdatedDt(now);
            mvnFcDao.update(mvnFc);

			// 퇴실신청의 입주업체퇴실여부를 'true'로 변경한다.
            checkoutReq.setMvnCheckoutYn(Boolean.TRUE);
            checkoutReq.setUpdaterId(workerId);
            checkoutReq.setUpdatedDt(now);
            checkoutDao.update(checkoutReq);

            // 퇴실처리 이력을 남긴다.
        	UsptMvnChcktHist hist = UsptMvnChcktHist.builder()
											.histId(string.getNewId(CodeExt.prefixId.이력ID))
											.histDt(now)
											.checkoutReqId(checkoutReq.getCheckoutReqId())
											.workTypeNm("퇴실처리")
											.workCn("입주업체와 입주시설에 대해서 퇴실처리하였습니다.")
											.workerId(workerId)
											.build();
        	histList.add(hist);

        	checkoutPrcsCnt++;
		}

		// 퇴실처리 이력을 생성한다.
		if (histList.size() > 0) {
			checkoutHistDao.insertList(histList);
		}

		return checkoutPrcsCnt;
	}

}
