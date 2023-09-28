package aicluster.member.api.account.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountUnlockParam extends AccountParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4711622744236656662L;

	private String passwd1;
	private String passwd2;
}
