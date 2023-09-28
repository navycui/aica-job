package aicluster.framework.notification.nhn.sms;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseData {
	private String requestId;
	private String statusCode;
	private String senderGroupingKey;
	private List<SmsResponseResult> sendResultList;
	
	public List<SmsResponseResult> getSendResultList() {
		List<SmsResponseResult> sendResultList = new ArrayList<>();
		if(this.sendResultList != null) {
			sendResultList.addAll(this.sendResultList);
		}
		return sendResultList;
	}

	public void setSendResultList(List<SmsResponseResult> sendResultList) {
		this.sendResultList = new ArrayList<>();
		if(sendResultList != null) {
			this.sendResultList.addAll(sendResultList);
		}
	}
}
