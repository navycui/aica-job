package aicluster.framework.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NiceIdEncDataParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2236914261683167007L;
	private String successUrl;
	private String failUrl;
}
