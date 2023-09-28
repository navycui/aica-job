package aicluster.framework.notification.nhn.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse {
	private EmailResponseHeader header;
	private EmailResponseBody body;
}
