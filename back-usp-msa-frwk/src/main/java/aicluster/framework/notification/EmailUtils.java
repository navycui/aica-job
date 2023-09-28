package aicluster.framework.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.config.EmailConfig;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.nhn.email.EmailParam;
import aicluster.framework.notification.nhn.email.EmailReceiver;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import aicluster.framework.notification.nhn.email.EmailResponse;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.service.NotificationService;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class EmailUtils {
	@Autowired
	private EmailConfig emailConfig;

	@Autowired
	private NotificationService notificationService;

	public EmailResult send(EmailSendParam param) {
		String url = emailConfig.getUrl() + "/email/v2.0/appKeys/{appKey}/sender/eachMail";
		String appKey = emailConfig.getAppKey();
		String secretKey = emailConfig.getSecretKey();
		String senderEmail = emailConfig.getSenderEmail();
		String senderNm = emailConfig.getSenderName();

		if (string.isNotBlank(param.getSenderEmail())) {
			senderEmail = param.getSenderEmail();
			senderNm = param.getSenderName();
		}

		if (param.getRecipientList() == null || param.getRecipientList().size() == 0) {
			throw new InvalidationException("수신자 이메일을 입력하세요.");
		}
		if (string.isBlank(param.getTitle())) {
			throw new InvalidationException("이메일 제목을 입력하세요.");
		}
		if (string.isBlank(param.getEmailCn())) {
			throw new InvalidationException("이메일 내용을 입력하세요.");
		}
		if (param.getContentHtml() == null) {
			param.setContentHtml(false);
		}

		EmailParam emailParam = new EmailParam();
		emailParam.setSenderAddress(senderEmail);
		emailParam.setSenderName(senderNm);
		emailParam.setTitle(param.getTitle());
		emailParam.setBody(param.getEmailCn());
		emailParam.isHtml(param.getContentHtml());
		emailParam.setReceiverList(param.getRecipientList());

		HttpResponse<EmailResponse> res = Unirest.post(url)
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Secret-Key", secretKey)
				.routeParam("appKey", appKey)
				.body(emailParam)
				.asObject(EmailResponse.class);

		EmailResult emailResult = new EmailResult();
		if (res.getStatus() != 200) {
			emailResult.setResultCode(-1);
			emailResult.setResultMessage("이메일 시스템 연동 실패");
			emailResult.setTotalCnt(param.getRecipientList().size());
			emailResult.setSuccessCnt(0);
			emailResult.setFailCnt(param.getRecipientList().size());
			for (EmailReceiver er : param.getRecipientList()) {
				EmailReceiverResult err = new EmailReceiverResult();
				err.setReceiveMailAddr(er.getReceiveMailAddr());
				err.setReceiveName(er.getReceiveName());
				err.setReceiveType(er.getReceiveType());
				err.setIsSuccessful(false);
				err.setResultCode(-1);
				err.setResultMessage("이메일 시스템 연동 실패");
				emailResult.addReceiverResult(err);
			}
		}
		else {
			EmailResponse er = res.getBody();
			emailResult.setEmailResponse(er);
		}

		// DB에 저장
		String emailId = notificationService.addEmailLog(param, emailResult);

		emailResult.setEmailId(emailId);
		return emailResult;
	}

}
