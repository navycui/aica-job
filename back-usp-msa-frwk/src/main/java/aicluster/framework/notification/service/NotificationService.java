package aicluster.framework.notification.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.CmmtEmailSndngDtlsDao;
import aicluster.framework.common.dao.CmmtEmailRcverDao;
import aicluster.framework.common.dao.CmmtSmsSndngDtlsDao;
import aicluster.framework.common.dao.CmmtSmsRcverDao;
import aicluster.framework.common.entity.CmmtEmailSndngDtls;
import aicluster.framework.common.entity.CmmtEmailRcver;
import aicluster.framework.common.entity.CmmtSmsSndngDtls;
import aicluster.framework.common.entity.CmmtSmsRcver;
import aicluster.framework.config.EmailConfig;
import aicluster.framework.config.SmsConfig;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiver;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsRecipient;
import aicluster.framework.notification.nhn.sms.SmsRecipientResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;

@Service("FwNotificationService")
public class NotificationService {

	@Autowired
	private SmsConfig smsConfig;

	@Autowired
	private EmailConfig emailConfig;

	@Autowired
	private CmmtSmsSndngDtlsDao cmmtSmsDao;

	@Autowired
	private CmmtSmsRcverDao cmmtSmsRecipientDao;

	@Autowired
	private CmmtEmailSndngDtlsDao cmmtEmailDao;

	@Autowired
	private CmmtEmailRcverDao cmmtEmailRecipientDao;

	public String addSmsLog(SmsSendParam param, SmsResult smsResult) {
		// cmmt_sms
		Date now = new Date();
		String smsId = string.getNewId("sms-");
		CmmtSmsSndngDtls cmmtSms = CmmtSmsSndngDtls.builder()
				.smsId(smsId)
				.senderMobileNo(smsConfig.getSenderNo())
				.smsCn(param.getSmsCn())
				.createdDt(now)
				.build();
		cmmtSmsDao.insert(cmmtSms);

		// cmmt_sms_recipient
		List<CmmtSmsRcver> csrList = new ArrayList<>();
		CmmtSmsRcver csr = null;
		SmsRecipient sr = null;
		for (SmsRecipientResult srr : smsResult.getRecipientList()) {
			csr = CmmtSmsRcver.builder()
					.smsId(smsId)
					.mobileNo(srr.getRecipientNo())
					.param(null)
					.success(srr.getIsSuccessful())
					.resultCode(srr.getResultCode())
					.resultMessage(srr.getResultMessage())
					.createdDt(now)
					.build();
			 sr = findRecipient(param.getRecipientList(), srr.getRecipientNo());
			 if (sr != null) {
				 String jsonStr = json.toString(sr.getTemplateParameter());
				 csr.setParam(jsonStr);
			 }

			 csrList.add(csr);
		}

		if (csrList.size() > 0) {
			cmmtSmsRecipientDao.insertList(csrList);
		}

		return smsId;
	}

	private SmsRecipient findRecipient(List<SmsRecipient> list, String mobileNo) {
		for (SmsRecipient sr : list) {
			if (string.equals(sr.getRecipientNo(), mobileNo)) {
				return sr;
			}
		}
		return null;
	}

	public String addEmailLog(EmailSendParam param, EmailResult emailResult) {

		String senderEmail = emailConfig.getSenderEmail();
		String senderNm = emailConfig.getSenderName();

		// cmmt_email
		Date now = new Date();
		String emailId = string.getNewId("email-");
		CmmtEmailSndngDtls cmmtEmail = CmmtEmailSndngDtls.builder()
				.emailId(emailId)
				.title(param.getTitle())
				.emailCn(param.getEmailCn())
				.html(param.getContentHtml())
				.senderEmail(senderEmail)
				.senderNm(senderNm)
				.createdDt(now)
				.build();
		cmmtEmailDao.insert(cmmtEmail);

		// cmmt_email_recipient
		List<CmmtEmailRcver> cerList = new ArrayList<>();
		CmmtEmailRcver cer = null;
		EmailReceiver rec = null;
		for (EmailReceiverResult err : emailResult.getReceiverList()) {
			cer = CmmtEmailRcver.builder()
					.emailId(emailId)
					.email(err.getReceiveMailAddr())
					.recipientNm(err.getReceiveName())
					.param(null)
					.success(err.getIsSuccessful())
					.resultCode(err.getResultCode())
					.resultMessage(err.getResultMessage())
					.createdDt(now)
					.build();

			 rec = findReceiver(param.getRecipientList(), err.getReceiveMailAddr());
			 if (rec != null) {
				 String jsonStr = json.toString(rec.getTemplateParameter());
				 cer.setParam(jsonStr);
			 }

			 cerList.add(cer);
		}

		if (cerList.size() > 0) {
			cmmtEmailRecipientDao.insertList(cerList);
		}

		return emailId;
	}

	private EmailReceiver findReceiver(List<EmailReceiver> list, String email) {
		for (EmailReceiver er : list) {
			if (string.equals(er.getReceiveMailAddr(), email)) {
				return er;
			}
		}
		return null;
	}

}
