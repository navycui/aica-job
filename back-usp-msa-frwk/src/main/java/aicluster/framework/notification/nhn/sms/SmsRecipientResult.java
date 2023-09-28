package aicluster.framework.notification.nhn.sms;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRecipientResult implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 92317633414203814L;
	private String recipientNo;
	private Boolean isSuccessful;
	private Integer resultCode;
	private String resultMessage;
}
