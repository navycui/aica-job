package aicluster.batch.api.mvn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.common.dao.UsptMvnFcltyResveDao;
import aicluster.batch.common.dao.UsptMvnFcltyResveHistDao;
import aicluster.batch.common.entity.UsptMvnFcltyResve;
import aicluster.batch.common.entity.UsptMvnFcltyResveHist;
import bnet.library.util.CoreUtils.string;

@Service
public class MvnFcRsvtService {
	@Autowired
	private UsptMvnFcltyResveDao rsvtDao;
	@Autowired
	private UsptMvnFcltyResveHistDao histDao;

	/**
	 * 이용시간이 지난 승인된 예약정보를 자동으로 이용종료 처리한다.
	 */
	public int updateRsvtClose() {
		Date now = new Date();
		String workerId = "BATCH";

		// 1.이용 종료시간이 현재시간을 기준으로 1시간전인 시설 예약정보를 조회한다.
		List<UsptMvnFcltyResve> list = rsvtDao.selectList();

		if (list.isEmpty()) {
			return 0;
		}

		// 2.예약정보를 이용종(CLOSE) 상태로 변경하고 이력 생성
		List<UsptMvnFcltyResveHist> histList = new ArrayList<>();
		for (UsptMvnFcltyResve rsvt:list) {
			// 2-1. 예약정보 이용종료(CLOSE) 상태 변경
			rsvt.setReserveSt("CLOSE");
			rsvt.setReserveStDt(now);
			rsvt.setUpdaterId(workerId);
			rsvt.setUpdatedDt(now);

			UsptMvnFcltyResveHist hist = UsptMvnFcltyResveHist.builder()
										.histId(string.getNewId("hist-"))
										.histDt(now)
										.reserveId(rsvt.getReserveId())
										.workTypeNm("이용종료")
										.workCn("이용종료 처리하였습니다.(배치)")
										.workerId(workerId)
										.build();
			histList.add(hist);
		}

		// 3.예약정보 update
		rsvtDao.updateList(list);

		// 4.이력정보 insert
		histDao.insertList(histList);

		return list.size();
	}
}
