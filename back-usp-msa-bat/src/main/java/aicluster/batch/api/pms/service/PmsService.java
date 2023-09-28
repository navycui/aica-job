package aicluster.batch.api.pms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.common.dao.CmmtCodeDao;
import aicluster.batch.common.dao.LmsEduCateTDao;
import aicluster.batch.common.dao.UsptBsnsPblancDao;
import aicluster.batch.common.entity.CmmtCode;
import aicluster.batch.common.entity.LmsEduCateT;
import aicluster.batch.common.entity.UsptBsnsPblanc;
import aicluster.framework.batch.BatchReturn;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PmsService {

	@Autowired
	private UsptBsnsPblancDao usptBsnsPblancDao;
	@Autowired
	private LmsEduCateTDao lmsEduCateTDao;
	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	/**
	 * 게시처리
	 * @return
	 */
	public BatchReturn updateNtce() {
		Date now = new Date();
		String workerId = "BATCH";
		String today = CoreUtils.date.format(now, "yyyyMMdd");

		int cnt = 0;
		int trgetCnt = 0;

		List<UsptBsnsPblanc> list = usptBsnsPblancDao.selectNtceAtList(today);
		if(list != null && !list.isEmpty()) {
			trgetCnt = list.size();
			log.debug("모집공고 게시처리 대상 건수 : {}", list.size());
			for(UsptBsnsPblanc info : list) {
				info.setUpdtDt(now);
				info.setUpdusrId(workerId);
				cnt += usptBsnsPblancDao.updateNtceAt(info);
			}
		}
		log.debug("모집공고 게시처리 처리 건수 : {}", cnt);

		return BatchReturn.success(String.format("모집공고 게시처리 대상:%d, 처리:%d", trgetCnt, cnt));
	}



	/**
	 * 접수마감처리
	 * @return
	 */
	public BatchReturn updateRceptClosing() {
		Date now = new Date();
		String workerId = "BATCH";
		String today = CoreUtils.date.format(now, "yyyyMMdd");
		String hour = CoreUtils.date.format(now, "HH");

		log.debug("처리 접수종료일자 : {}", today);
		log.debug("처리 마감시간 : {}", hour);

		int cnt = 0;
		int trgetCnt = 0;
		if(CoreUtils.string.equals("00",hour)) {
			Calendar calendar = new GregorianCalendar();
			calendar.add(Calendar.DATE, -1);
			String yesterday = CoreUtils.date.format(calendar.getTime(), "yyyyMMdd");

			List<UsptBsnsPblanc> yesterdayList = usptBsnsPblancDao.selectRceptClosingListOfYesterday(yesterday);
			if(yesterdayList != null && !yesterdayList.isEmpty()) {
				trgetCnt += yesterdayList.size();
				log.debug("모집공고 접수마감처리 대상 건수(전날) : {}", yesterdayList.size());
				for(UsptBsnsPblanc info : yesterdayList) {
					info.setUpdtDt(now);
					info.setUpdusrId(workerId);
					cnt += usptBsnsPblancDao.updateRceptClosing(info);
				}
			}
		}

		List<UsptBsnsPblanc> list = usptBsnsPblancDao.selectRceptClosingList(today, hour);
		if(list != null && !list.isEmpty()) {
			trgetCnt += list.size();
			log.debug("모집공고 접수마감처리 대상 건수 : {}", list.size());
			for(UsptBsnsPblanc info : list) {
				info.setUpdtDt(now);
				info.setUpdusrId(workerId);
				cnt += usptBsnsPblancDao.updateRceptClosing(info);
			}
		}
		log.debug("모집공고 접수마감처리 건수 : {}", cnt);

		return BatchReturn.success(String.format("모집공고 게시처리 대상:%d, 처리:%d", trgetCnt, cnt));
	}



	/**
	 * LMS 에서 카테고리 정보를 조회해서 공통코드 테이블에 저장
	 * @return
	 */
	public BatchReturn updateLmsCategory() {
		Date now = new Date();
		String workerId = "BATCH";
		List<LmsEduCateT> eduCateList = lmsEduCateTDao.selectList();
		CmmtCode code = new CmmtCode();
		code.setCodeGroup("RECOMEND_CL");
		code.setCodeType("EDU");
		code.setEnabled(true);
		code.setCreatedDt(now);
		code.setCreatorId(workerId);
		code.setUpdatedDt(now);
		code.setUpdaterId(workerId);

		for(LmsEduCateT info : eduCateList) {
			code.setCode(info.getCateId());
			code.setCodeNm(info.getTitle());
			if(CoreUtils.string.isBlank(info.getOrderCd())){
				code.setSortOrder(1L);
			} else {
				if(CoreUtils.string.isNumeric(info.getOrderCd())){
					code.setSortOrder(Long.valueOf(info.getOrderCd()));
				} else {
					code.setSortOrder(0L);
				}
			}
			cmmtCodeDao.insert(code);
		}
		return BatchReturn.success("LMS 카테고리 정보 업데이트");
	}
}
