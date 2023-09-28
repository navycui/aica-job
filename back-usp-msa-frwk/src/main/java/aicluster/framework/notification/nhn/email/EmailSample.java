package aicluster.framework.notification.nhn.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bnet.library.exception.InvalidationException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class EmailSample {
	public void send() {
		String url = "https://api-mail.cloud.toast.com/email/v2.0/appKeys/{appKey}/sender/eachMail";
		String appKey = "lCQTV8avjXfRfmcU";
		String secretKey = "i8v3IvXg";

		List<EmailReceiver> receiverList = new ArrayList<>();
		EmailReceiver er = new EmailReceiver();
		er.setReceiveMailAddr("stargatex@gmail.com");
		er.setReceiveName("유영민");
		receiverList.add(er);

		er = new EmailReceiver();
		er.setReceiveMailAddr("stargatex@naver.com");
		er.setReceiveName("유영민");
		receiverList.add(er);

		EmailParam emailParam = new EmailParam();
		emailParam.setSenderAddress("stargatex@gmail.com");
		emailParam.setSenderName("유명한");
		emailParam.setTitle("시험용입니다.");
		emailParam.setBody("시험용입니다.\n시험용입니다.\n시험용입니다.");
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "text/plain");
		emailParam.setCustomHeaders(headers);
		emailParam.setReceiverList(receiverList);

		HttpResponse<EmailResponse> res = Unirest.post(url)
				.header("Content-Type", "application/json;charset=UTF-8")
				.header("X-Secret-Key", secretKey)
				.routeParam("appKey", appKey)
				.body(emailParam)
				.asObject(EmailResponse.class);

		if (res.getStatus() != 200) {
			throw new InvalidationException("오류");
		}

		EmailResponse emailResponse = res.getBody();
		System.out.println("====");
		System.out.println(emailResponse.getHeader());
		System.out.println("====");
		System.out.println(emailResponse.getBody());
	}

//	public static void main(String[] args) {
//		EmailSample sample = new EmailSample();
//		sample.send();
//	}
}
