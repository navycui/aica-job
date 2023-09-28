package aicluster.framework.notification.nhn.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {
	private SmsResponseHeader header;
	private SmsResponseBody body;
}
