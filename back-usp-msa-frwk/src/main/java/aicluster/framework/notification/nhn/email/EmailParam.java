package aicluster.framework.notification.nhn.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class EmailParam {
	private String senderAddress;
	private String senderName;
	private String requestDate;
	private String title;
	private String body;
	private String userId;
	private Map<String, String> customHeaders = new HashMap<>();
	private List<EmailReceiver> receiverList;

	public EmailParam() {
		isHtml(false);
	}

	public void isHtml(boolean isHtml) {
		if (isHtml) {
			customHeaders.put("Content-Type", "text/html; charset=UTF-8");
		}
		else {
			customHeaders.put("Content-Type", "text/plain; charset=UTF-8");
		}
	}
	
	public void setCustomHeaders(Map<String, String> customHeaders) {
		this.customHeaders = new HashMap<>();
		if(customHeaders != null) {
			this.customHeaders = new HashMap<>(customHeaders.size());
			for(String key:customHeaders.keySet()){
				this.customHeaders.put(key, customHeaders.get(key));
			}
		}
	}

	public Map<String, String> getCustomHeaders() {
		Map<String, String> customHeaders = new HashMap<>();
		for(String key:this.customHeaders.keySet()){
			customHeaders.put(key, this.customHeaders.get(key));
		}
		return customHeaders;
	}
	
	public List<EmailReceiver> getReceiverList() {
		List<EmailReceiver> receiverList = new ArrayList<>();
		if(this.receiverList != null) {
			receiverList.addAll(this.receiverList);
		}
		return receiverList;
	}
	
	public void setReceiverList(List<EmailReceiver> receiverList) {
		this.receiverList = new ArrayList<>();
		if(receiverList != null) {
			this.receiverList.addAll(receiverList);
		}
	}
}
