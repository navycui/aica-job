package aicluster.sample.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.common.entity.TokenDto;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.framework.security.JwtUtils;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;

@RestController
public class SampleController {
	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private SmsUtils smsUtils;

	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping("/api/email")
	public EmailResult email() {
		String emailCn = CoreUtils.file.readFileString("/form/email/email-sample.html");
		EmailSendParam param = new EmailSendParam();
		param.setContentHtml(true);
		param.setEmailCn(emailCn);
		param.setTitle("(AICA) Sample");
		Map<String, String> templ = new HashMap<>();
		templ.put("memberNm", "유영민");
		templ.put("loginId", "patrick");
		templ.put("passwd", "1234");
		param.addRecipient("ymyoo@brainednet.com", "유영민", templ);

		templ = new HashMap<>();
		templ.put("memberNm", "유영민1");
		templ.put("loginId", "patrick1");
		templ.put("passwd", "12341");
		param.addRecipient("stargatex@gmail.com", "유영민", templ);

		EmailResult er = emailUtils.send(param);
		return er;
	}

	@GetMapping("/api/sms")
	public SmsResult sms() {
		String smsCn = CoreUtils.file.readFileString("/form/sms/phone-sample.txt");
		if (string.isBlank(smsCn)) {
			throw new LoggableException("SMS 발송용 본문파일을 읽을 수 없습니다.");
		}

		SmsSendParam param = new SmsSendParam();
		param.setSmsCn(smsCn);
		Map<String, String> templ = new HashMap<>();
		templ.put("certNo", "1234");
		param.addRecipient("01026420320", templ);
		SmsResult sr = smsUtils.send(param);

		return sr;
	}

	@GetMapping("/api/mms")
	public SmsResult mms() {
		SmsSendParam param = new SmsSendParam();
		param.setSmsCn("일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십");
		param.addRecipient("01026420320");
		SmsResult sr = smsUtils.send(param);

		return sr;
	}

	@GetMapping("/api/refresh")
	public List<String> refresh() {
		List<String> list = new ArrayList<>();
		String[] refreshTokens = {
				"eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTU0Mzk5NzJ9.u27Pzf6VpM34yHC3zluRLN9yo6_sVHzZeLH6NFCwindBCFAuyqAhT9J1Kyk8fnO1dj5Ubw4Vs6hcdwr25l_JaQ",
				"eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTU0Mzk4OTF9.IAqMeoYm5PQH_-YRuQTOK2eC3uYm4qj4JjCOa00CRL5oi9UC6jC7SH4OBFb0oxN4WN6jkxA9geYvIRoEpZ5ETg"
			};

		int idx = 0;
		for (int i = 0; i < 10; i++) {
			idx = i % 2;
			TokenDto dto = jwtUtils.refreshToken_member(refreshTokens[idx]);
			refreshTokens[idx] = dto.getRefreshToken();
			list.add(refreshTokens[idx]);
		}

		return list;
	}
}
