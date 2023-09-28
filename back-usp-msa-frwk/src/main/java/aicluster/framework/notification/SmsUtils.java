package aicluster.framework.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.config.SmsConfig;
import aicluster.framework.notification.dto.SmsCertParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.sms.MmsParam;
import aicluster.framework.notification.nhn.sms.SmsParam;
import aicluster.framework.notification.nhn.sms.SmsRecipient;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResponse;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.notification.service.NotificationService;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class SmsUtils {

	@Autowired
	private SmsConfig smsConfig;

	@Autowired
	private NotificationService notificationService;

	public SmsResult send(SmsSendParam param) {
		if (string.isBlank(param.getSenderNo())) {
			param.setSenderNo(smsConfig.getSenderNo());
		}

		if (param.getRecipientList() == null || param.getRecipientList().size() == 0) {
			throw new InvalidationException("SMS 수신번호를 입력하세요.");
		}

		if (string.isBlank(param.getSmsCn())) {
			throw new InvalidationException("SMS 본문을 입력하세요.");
		}
		int smsCnLen = string.length(param.getSmsCn());
		if ( smsCnLen > 1000) {
			throw new InvalidationException("SMS 본문은 1,000자를 넘을 수 없습니다.");
		}

		boolean mms = false;
		if (smsCnLen > 45) {
			mms = true;
		}

		if (!mms) {
			return sendSMS(param);
		}

		return sendMMS(param);
	}

	private SmsResult sendSMS(SmsSendParam param) {
		String url = smsConfig.getUrl() + "/sms/v3.0/appKeys/{appKey}/sender/sms";
		String appKey = smsConfig.getAppKey();
		String secretKey = smsConfig.getSecretKey();
		String senderNo = param.getSenderNo();

		SmsParam sp = new SmsParam();
		sp.setSendNo(senderNo);
		sp.setBody(param.getSmsCn());
		sp.setRecipientList(param.getRecipientList());

		HttpResponse<SmsResponse> res = Unirest.post(url)
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Secret-Key", secretKey)
				.routeParam("appKey", appKey)
				.body(sp)
				.asObject(SmsResponse.class);

		SmsResult smsResult = new SmsResult();
		if (res.getStatus() != 200) {
			smsResult.setResultCode(-1);
			smsResult.setResultMessage("SMS시스템 연동실패");
			smsResult.setTotalCnt(param.getRecipientList().size());
			smsResult.setSuccessCnt(0);
			smsResult.setFailCnt(param.getRecipientList().size());
			for (SmsRecipient sr : param.getRecipientList()) {
				SmsRecipientResult srr = new SmsRecipientResult();
				srr.setIsSuccessful(false);
				srr.setRecipientNo(sr.getRecipientNo());
				srr.setResultCode(-1);
				srr.setResultMessage("SMS시스템 연동실패");
				smsResult.addRecipientResult(srr);
			}
		}
		else {
			SmsResponse smsResponse = res.getBody();
			smsResult.setSmsResponse(smsResponse);
		}

		// DB에 저장
		String smsId = notificationService.addSmsLog(param, smsResult);

		smsResult.setSmsId(smsId);
		return smsResult;
	}

	private SmsResult sendMMS(SmsSendParam param) {
		String url = smsConfig.getUrl() + "/sms/v3.0/appKeys/{appKey}/sender/mms";
		String appKey = smsConfig.getAppKey();
		String secretKey = smsConfig.getSecretKey();
		String senderNo = param.getSenderNo();

		MmsParam sp = new MmsParam();
		sp.setTitle("인공지능산업융합사업단");
		sp.setSendNo(senderNo);
		sp.setBody(param.getSmsCn());
		sp.setRecipientList(param.getRecipientList());

		HttpResponse<SmsResponse> res = Unirest.post(url)
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Secret-Key", secretKey)
				.routeParam("appKey", appKey)
				.body(sp)
				.asObject(SmsResponse.class);

		SmsResult smsResult = new SmsResult();
		if (res.getStatus() != 200) {
			smsResult.setResultCode(-1);
			smsResult.setResultMessage("SMS시스템 연동실패");
			smsResult.setTotalCnt(param.getRecipientList().size());
			smsResult.setSuccessCnt(0);
			smsResult.setFailCnt(param.getRecipientList().size());
			for (SmsRecipient sr : param.getRecipientList()) {
				SmsRecipientResult srr = new SmsRecipientResult();
				srr.setIsSuccessful(false);
				srr.setRecipientNo(sr.getRecipientNo());
				srr.setResultCode(-1);
				srr.setResultMessage("SMS시스템 연동실패");
				smsResult.addRecipientResult(srr);
			}
		}
		else {
			SmsResponse smsResponse = res.getBody();
			smsResult.setSmsResponse(smsResponse);
		}

		// DB에 저장
		String smsId = notificationService.addSmsLog(param, smsResult);

		smsResult.setSmsId(smsId);
		return smsResult;
	}

	/**
	 * 인증을 위한 SMS 즉시 발송
	 * 본문 내용에 인증관련 문구가 없을 경우 발송실패할 수 있음.
	 *
	 * @param param SMS인증용 파라미터
	 * @return 처리결과
	 */
	public SmsResult sendCert(SmsCertParam param) {
		String url = smsConfig.getUrl() + "/sms/v3.0/appKeys/{appKey}/sender/auth/sms";
		String appKey = smsConfig.getAppKey();
		String secretKey = smsConfig.getSecretKey();
		String senderNo = smsConfig.getSenderNo();

		if (param.getRecipientList() == null || param.getRecipientList().size() == 0) {
			throw new InvalidationException("SMS 수신자 정보를 입력하세요.");
		}

		if (string.isBlank(param.getSmsCn())) {
			throw new InvalidationException("SMS 본문을 입력하세요.");
		}

		if (string.length(param.getSmsCn()) > 45) {
			throw new InvalidationException("SMS 본문은 45자를 넘을 수 없습니다.");
		}

		SmsParam sp = new SmsParam();
		sp.setSendNo(senderNo);
		sp.setBody(param.getSmsCn());
		sp.setRecipientList(param.getRecipientList());

		HttpResponse<SmsResponse> res = Unirest.post(url)
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Secret-Key", secretKey)
				.routeParam("appKey", appKey)
				.body(sp)
				.asObject(SmsResponse.class);

		if (res.getStatus() != 200) {
			SmsResult smsResult = new SmsResult();
			smsResult.setResultCode(-1);
			smsResult.setResultMessage("SMS시스템 연동실패");
			smsResult.setTotalCnt(param.getRecipientList().size());
			smsResult.setSuccessCnt(0);
			smsResult.setFailCnt(param.getRecipientList().size());
			for (SmsRecipient sr : param.getRecipientList()) {
				SmsRecipientResult srr = new SmsRecipientResult();
				srr.setIsSuccessful(false);
				srr.setRecipientNo(sr.getRecipientNo());
				srr.setResultCode(-1);
				srr.setResultMessage("SMS시스템 연동실패");
			}

			return smsResult;
		}

		SmsResponse smsResponse = res.getBody();
		SmsResult smsResult = new SmsResult();
		smsResult.setSmsResponse(smsResponse);

		return smsResult;
	}
}
