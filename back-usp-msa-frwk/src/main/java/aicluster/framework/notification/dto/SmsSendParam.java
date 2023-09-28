package aicluster.framework.notification.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aicluster.framework.notification.nhn.sms.SmsRecipient;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendParam {
	private String senderNo;
	private String smsCn;
	private List<SmsRecipient> recipientList = new ArrayList<>();
	private List<String> mobileNoList = new ArrayList<>();

	public void clearRecipientList() {
		this.recipientList.clear();
		this.mobileNoList.clear();
	}

	public void addRecipient(String mobileNo) {
		mobileNo = string.getNumberOnly(mobileNo);
		if (this.mobileNoList.contains(mobileNo)) {
			return;
		}
		if (string.isBlank(mobileNo)) {
			return;
		}
		SmsRecipient sr = new SmsRecipient();
		sr.setRecipientNo(mobileNo);
		this.recipientList.add(sr);
		this.mobileNoList.add(mobileNo);
	}

	public void addRecipient(SmsRecipient recipient) {
		String mobileNo = string.getNumberOnly(recipient.getRecipientNo());
		if (this.mobileNoList.contains(mobileNo)) {
			return;
		}
		if (string.isBlank(mobileNo)) {
			return;
		}
		recipient.setRecipientNo(mobileNo);
		recipientList.add(recipient);
		this.mobileNoList.add(mobileNo);
	}

	public void addRecipient(String mobileNo, Map<String, String> templateParameter) {
		mobileNo = string.getNumberOnly(mobileNo);
		if (this.mobileNoList.contains(mobileNo)) {
			return;
		}
		if (string.isBlank(mobileNo)) {
			return;
		}
		SmsRecipient sr = new SmsRecipient(mobileNo, null, templateParameter);
		recipientList.add(sr);
		this.mobileNoList.add(mobileNo);
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
	
	public List<String> getMobileNoList() {
		List<String> mobileNoList = new ArrayList<>();
		if(this.mobileNoList != null) {
			mobileNoList.addAll(this.mobileNoList);
		}
		return mobileNoList;
	}

	public void setMobileNoList(List<String> mobileNoList) {
		this.mobileNoList = new ArrayList<>();
		if(mobileNoList != null) {
			this.mobileNoList.addAll(mobileNoList);
		}
	}
}
