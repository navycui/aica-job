package aicluster.framework.notification.nhn.sms;

import java.util.HashMap;
import java.util.Map;

import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsRecipient {
	private String recipientNo;
	private String countryCode = "82";
	private Map<String, String> templateParameter;

	public String getRecipientNo() {
		return recipientNo;
	}
	public void setRecipientNo(String recipientNo) {
		this.recipientNo = string.getNumberOnly(recipientNo);
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Map<String, String> getTemplateParameter() {
		if (templateParameter == null) {
			templateParameter = new HashMap<>();
		}
		return templateParameter;
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
	public void putTemplateParameter(String key, String value) {
		if (templateParameter == null) {
			templateParameter = new HashMap<>();
		}
		templateParameter.put(key, value);
	}
}
