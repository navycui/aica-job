package aicluster.framework.notification.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aicluster.framework.notification.nhn.email.EmailReceiver;
import aicluster.framework.notification.nhn.email.EmailReceiverResult;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendParam {
	private String senderEmail;
	private String senderName;
	private String title;
	private String emailCn;
	private Boolean contentHtml = false;
	private List<EmailReceiver> recipientList = new ArrayList<>();
	private List<String> emailList = new ArrayList<>();

	public void clearRecipientList() {
		this.recipientList.clear();
		this.emailList.clear();
	}

	public void addRecipient(String email, String memberNm) {
		email = string.deleteWhitespace(email);
		email = string.lowerCase(email);
		memberNm = string.trim(memberNm);

		if (string.isBlank(email)) {
			return;
		}
		if (emailList.contains(email)) {
			return;
		}
		if (string.isBlank(memberNm)) {
			memberNm = email;
		}
		EmailReceiver er = new EmailReceiver(email, memberNm);
		this.recipientList.add(er);
		this.emailList.add(email);
	}

	public void addRecipient(String email, String memberNm, Map<String, String> templateParameter) {
		email = string.deleteWhitespace(email);
		email = string.lowerCase(email);
		memberNm = string.trim(memberNm);

		if (string.isBlank(email)) {
			return;
		}
		if (emailList.contains(email)) {
			return;
		}
		if (string.isBlank(memberNm)) {
			memberNm = email;
		}
		EmailReceiver er = new EmailReceiver(email, memberNm, templateParameter);
		recipientList.add(er);
		this.emailList.add(email);
	}
	
	public List<EmailReceiver> getRecipientList() {
		List<EmailReceiver> recipientList = new ArrayList<>();
		if(this.recipientList != null) {
			recipientList.addAll(this.recipientList);
		}
		return recipientList;
	}
	
	public void setRecipientList(List<EmailReceiver> recipientList) {
		this.recipientList = new ArrayList<>();
		if(recipientList != null) {
			this.recipientList.addAll(recipientList);
		}
	}
	
	public List<String> getEmailList() {
		List<String> emailList = new ArrayList<>();
		if(this.emailList != null) {
			emailList.addAll(this.emailList);
		}
		return emailList;
	}
	
	public void setEmailList(List<String> emailList) {
		this.emailList = new ArrayList<>();
		if(emailList != null) {
			this.emailList.addAll(emailList);
		}
	}
}
