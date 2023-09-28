package aicluster.framework.notification.nhn.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseHeader {
	private Integer resultCode;
	private String resultMessage;
	private Boolean isSuccessful;
}
