package aicluster.framework.notification.nhn.email;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailReceiverResult implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5281543511874732030L;
	private String receiveMailAddr;
    private String receiveName;
    private String receiveType;
    private Integer resultCode;
    private String resultMessage;
	private Boolean isSuccessful;
}
