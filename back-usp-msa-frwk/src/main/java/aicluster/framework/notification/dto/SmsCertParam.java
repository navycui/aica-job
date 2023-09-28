package aicluster.framework.notification.dto;

import java.util.ArrayList;
import java.util.List;

import aicluster.framework.notification.nhn.sms.SmsRecipient;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCertParam {
	private String smsCn;
	private List<SmsRecipient> recipientList;

	public void addRecipient(String mobileNo, String key, String value) {
		if (recipientList == null) {
			recipientList = new ArrayList<>();
		}
		mobileNo = string.getNumberOnly(mobileNo);
		if (string.isNotBlank(mobileNo) && string.isNotBlank(key) && string.isNotBlank(value)) {
			SmsRecipient sr = new SmsRecipient();
			sr.setRecipientNo(mobileNo);
			sr.putTemplateParameter(key, value);
			recipientList.add(sr);
		}
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
