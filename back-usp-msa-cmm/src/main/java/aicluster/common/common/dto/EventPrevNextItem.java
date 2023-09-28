package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPrevNextItem implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4051528971257038748L;

	private String eventId;
	private String eventNm;
	private String prevEventId;
	private String prevEventNm;
	private String nextEventId;
	private String nextEventNm;
}
