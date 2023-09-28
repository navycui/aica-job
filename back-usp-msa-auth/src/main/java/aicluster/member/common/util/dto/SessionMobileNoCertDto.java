package aicluster.member.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionMobileNoCertDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6472733892942370578L;
	private String certNo;
	private String mobileNo;
	private boolean checked;
}
