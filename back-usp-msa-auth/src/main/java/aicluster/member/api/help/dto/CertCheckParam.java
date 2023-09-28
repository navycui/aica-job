package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertCheckParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -9043740953684363966L;
	private String key;
	private String certNo;
}
