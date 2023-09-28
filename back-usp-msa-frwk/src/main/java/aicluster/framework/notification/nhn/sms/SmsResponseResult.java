package aicluster.framework.notification.nhn.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponseResult {
	private String recipientNo;
	private Integer resultCode;
	private String resultMessage;
	private Integer recipientSeq;
	private String recipientGroupingKey;
}
