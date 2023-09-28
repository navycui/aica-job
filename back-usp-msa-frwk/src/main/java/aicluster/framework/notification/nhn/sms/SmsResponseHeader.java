package aicluster.framework.notification.nhn.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseHeader {
	private Boolean isSuccessful;
	private Integer resultCode;
	private String resultMessage;
}
