package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCertReqParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4437518349693530431L;
	private String key;
	private String email;
}
