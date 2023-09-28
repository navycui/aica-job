package aicluster.member.api.module.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NiceIdEncData implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1897947865777200312L;
	private String encData;
}
