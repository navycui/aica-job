package aicluster.framework.notification.nhn.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResult implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4552326989151674882L;
	private String emailId;
	private Integer resultCode;
	private String resultMessage;
	private Integer totalCnt;
	private Integer successCnt;
	private Integer failCnt;
	private List<EmailReceiverResult> receiverList;

	public void addReceiverResult(EmailReceiverResult srr) {
		if (receiverList == null) {
			receiverList = new ArrayList<>();
		}
		if (srr != null) {
			receiverList.add(srr);
		}
	}

	public void setEmailResponse(EmailResponse emailResponse) {
		EmailResponseHeader emailHeader = emailResponse.getHeader();
		EmailResponseBody emailBody = emailResponse.getBody();
		EmailResponseData emailData = emailBody.getData();
		List<EmailResponseResult> emailResultList = emailData.getResults();

		this.resultCode = emailHeader.getResultCode();
		this.resultMessage = emailHeader.getResultMessage();
		this.totalCnt = emailResultList.size();
		int successCnt = 0;
		int failCnt = 0;
		if (this.receiverList != null) {
			this.receiverList.clear();
		}
		for (EmailResponseResult result : emailResultList) {
			EmailReceiverResult srr = new EmailReceiverResult();
			boolean isSuccessful = result.getResultCode() == 0;
			srr.setIsSuccessful(isSuccessful);
			srr.setReceiveMailAddr(result.getReceiveMailAddr());
			srr.setReceiveName(result.getReceiveName());
			srr.setReceiveType(result.getReceiveType());
			srr.setResultCode(result.getResultCode());
			srr.setResultMessage(result.getResultMessage());
			this.addReceiverResult(srr);
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
	
	public List<EmailReceiverResult> getReceiverList() {
		List<EmailReceiverResult> receiverList = new ArrayList<>();
		if(this.receiverList != null) {
			receiverList.addAll(this.receiverList);
		}
		return receiverList;
	}
	
	public void setReceiverList(List<EmailReceiverResult> receiverList) {
		this.receiverList = new ArrayList<>();
		if(receiverList != null) {
			this.receiverList.addAll(receiverList);
		}
	}
}
