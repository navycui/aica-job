package aicluster.framework.notification.nhn.sms;

import java.util.ArrayList;
import java.util.List;

import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsParam {
	private String templateId;
	private String body;
	private String sendNo;
	private String senderGroupingKey;
	private String userId;
	private String statsId;
	private List<SmsRecipient> recipientList;

	public void setSendNo(String sendNo) {
		this.sendNo = string.getNumberOnly(sendNo);
	}
	
	public List<SmsRecipient> getRecipientList() {
		List<SmsRecipient> recipientList = new ArrayList<>();
		if(this.recipientList != null) {
			recipientList.addAll(this.recipientList);
		}
		return recipientList;
	}

	public void setRecipientList(List<SmsRecipient> recipientList) {
		this.recipientList = new ArrayList<>();
		if(recipientList != null) {
			this.recipientList.addAll(recipientList);
		}
	}
}
