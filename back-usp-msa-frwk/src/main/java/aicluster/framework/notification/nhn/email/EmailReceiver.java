package aicluster.framework.notification.nhn.email;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailReceiver {

	public static final String 수신 = "MRT0";
	public static final String 참조 = "MRT1";
	public static final String 숨은참조 = "MRT2";

	private String receiveMailAddr;
	private String receiveName;
	private String receiveType;
	private Map<String, String> templateParameter;

	public EmailReceiver(String receiveMailAddr, String receiveName) {
		this.receiveMailAddr = receiveMailAddr;
		this.receiveName = receiveName;
		this.receiveType = 수신;
	}

	public EmailReceiver(String receiveMailAddr, String receiveName, Map<String, String> templateParameter) {
		this.receiveMailAddr = receiveMailAddr;
		this.receiveName = receiveName;
		this.receiveType = 수신;
		this.templateParameter = templateParameter;
	}
	
	public void setTemplateParameter(Map<String, String> templateParameter) {
		this.templateParameter = new HashMap<>();
		if(templateParameter != null) {
			this.templateParameter = new HashMap<>(templateParameter.size());
			for(String key:templateParameter.keySet()){
				this.templateParameter.put(key, templateParameter.get(key));
			}
		}
	}

	public Map<String, String> getTemplateParameter() {
		Map<String, String> templateParameter = new HashMap<>();
		for(String key:this.templateParameter.keySet()){
			templateParameter.put(key, this.templateParameter.get(key));
		}
		return templateParameter;
	}
}
