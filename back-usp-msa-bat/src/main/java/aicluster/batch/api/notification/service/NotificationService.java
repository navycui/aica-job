package aicluster.batch.api.notification.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.common.dao.CmmtCodeDao;
import aicluster.batch.common.dao.CmmtSysChargerDao;
import aicluster.batch.common.dao.LogtErrorLogDao;
import aicluster.batch.common.entity.CmmtCode;
import aicluster.batch.common.entity.CmmtSysCharger;
import aicluster.batch.common.util.CodeExt;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;

@Service
public class NotificationService {
	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	@Autowired
	private LogtErrorLogDao logtErrorLogDao;

	@Autowired
	private CmmtSysChargerDao cmmtSystemPicDao;

	@Autowired
	private EmailUtils emailUtils;

	public long notifyError() {
		Date today = new Date();
		Date yesterday = date.addDays(today, -1);
		String strToday = date.format(today, "yyyyMMdd");
		String strYesterday = date.format(yesterday, "yyyyMMdd");

		// API시스템 목록 조회
		List<CmmtCode> apiSystemList = cmmtCodeDao.selectList_enabled(CodeExt.apiSystemId.CODE_GROUP, null);

		/*
		 * 시스템별 오류 건수 조회 및 알림
		 */
		String apiSystemId = null;
		long emailSendCnt = 0;
		String emailFilePath = "/form/email/email-error.html";
		String emailCn = CoreUtils.file.readFileString(emailFilePath);

		Map<String, String> templateParam = new HashMap<>();
		String day = date.format(today, "yyyy년 MM월 dd일 HH시 mm분");
		templateParam.put("day", day);
		for (CmmtCode code : apiSystemList) {
			apiSystemId = code.getCode();

			// 어제 오류 건수 조회
			long yesterdayCnt = logtErrorLogDao.selectCount(apiSystemId, strYesterday);

			// 오늘 오류 건수 조회
			long todayCnt = logtErrorLogDao.selectCount(apiSystemId, strToday);

			templateParam.put(apiSystemId + "_1", String.format("%,d", yesterdayCnt));
			templateParam.put(apiSystemId + "_2", String.format("%,d", todayCnt));
		}
		// 담당자 조회
		List<CmmtSysCharger> picList = cmmtSystemPicDao.selectAll();

		// 이메일 발송
		emailSendCnt += sendErrorEmail(emailCn, picList, templateParam);

		return emailSendCnt;
	}

	private int sendErrorEmail(String emailCn, List<CmmtSysCharger> picList, Map<String, String> templateParam) {
		Date now = new Date();
		String ymd = date.format(now, "[yyyy년 MM월 dd일] HH시");

		String title = String.format("(AICA) %s 시스템 오류 정보", ymd);

		EmailSendParam param = new EmailSendParam();
		param.setContentHtml(true);
		param.setTitle(title);
		param.setEmailCn(emailCn);

		List<String> emailList = new ArrayList<>();
		for (CmmtSysCharger pic : picList) {
			if (emailList.contains(pic.getPicEmail())) {
				continue;
			}
			param.addRecipient(pic.getPicEmail(), pic.getPicNm(), templateParam);
			emailList.add(pic.getPicEmail());
		}

		int sendCnt = (param.getRecipientList() == null) ? 0 : param.getRecipientList().size();
		if (sendCnt > 0) {
			emailUtils.send(param);
		}
		return sendCnt;
	}
}
