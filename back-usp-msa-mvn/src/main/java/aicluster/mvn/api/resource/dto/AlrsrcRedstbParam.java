package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcRedstbParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1746130544839926126L;

	private String alrsrcId;

	private List<AlrsrcDstbInsListItem> alrsrcDstbInsList;		/** 자원할당 입력목록 */

}
