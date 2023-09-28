package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptEvlTrget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvlPresnatnRequstDto implements Serializable {

	private static final long serialVersionUID = 4988861835530844392L;
	/** 사유내용 */
	String  resnCn;
	/** sms-email 선택 */
	String sendSmsEmail;
	/*대상자리스트*/
	List<UsptEvlTrget> usptEvlTrgetList;
}
