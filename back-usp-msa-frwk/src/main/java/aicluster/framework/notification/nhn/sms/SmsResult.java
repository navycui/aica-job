package aicluster.framework.notification.nhn.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResult implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -757640645642499390L;
	private String smsId;
	private Integer resultCode;
	private String resultMessage;
	private Integer totalCnt;
	private Integer successCnt;
	private Integer failCnt;
	private List<SmsRecipientResult> recipientList;

	public void addRecipientResult(SmsRecipientResult srr) {
		if (recipientList == null) {
			recipientList = new ArrayList<>();
		}
		if (srr != null) {
			recipientList.add(srr);
		}
	}

	public void setSmsResponse(SmsResponse smsResponse) {
		SmsResponseHeader smsHeader = smsResponse.getHeader();
		SmsResponseBody smsBody = smsResponse.getBody();
		SmsResponseData smsData = smsBody.getData();
		List<SmsResponseResult> smsResultList = smsData.getSendResultList();

		this.resultCode = smsHeader.getResultCode();
		this.resultMessage = smsHeader.getResultMessage();
		this.totalCnt = smsResultList.size();
		int successCnt = 0;
		int failCnt = 0;
		if (this.recipientList != null) {
			this.recipientList.clear();
		}
		for (SmsResponseResult result : smsResultList) {
			SmsRecipientResult srr = new SmsRecipientResult();
			boolean isSuccessful = result.getResultCode() == 0;
			srr.setIsSuccessful(isSuccessful);
			srr.setRecipientNo(result.getRecipientNo());
			srr.setResultCode(result.getResultCode());
			srr.setResultMessage(result.getResultMessage());
			this.addRecipientResult(srr);
			if (isSuccessful) {
				successCnt++;
			}
			else {
				failCnt++;
			}
		}

		this.setSuccessCnt(successCnt);
		this.setFailCnt(failCnt);
	}
	
	public List<SmsRecipientResult> getRecipientList() {
		List<SmsRecipientResult> recipientList = new ArrayList<>();
		if(this.recipientList != null) {
			recipientList.addAll(this.recipientList);
		}
		return recipientList;
	}

	public void setRecipientList(List<SmsRecipientResult> recipientList) {
		this.recipientList = new ArrayList<>();
		if(recipientList != null) {
			this.recipientList.addAll(recipientList);
		}
	}
}
