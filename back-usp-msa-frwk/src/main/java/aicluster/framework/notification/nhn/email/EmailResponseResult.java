package aicluster.framework.notification.nhn.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseResult {
	private String receiveMailAddr;
    private String receiveName;
    private String receiveType;
    private Integer resultCode;
    private String resultMessage;
}
