package aicluster.common.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtEventCn implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8052461448022090233L;
	private String eventCnId;
	private String eventId;
	private Long sortOrder;
	private String header;
	private String articleCn;
}
