package aicluster.mvn.api.company.dto;

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
public class MvnCmpnyAlarmParam implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 8140175677587180557L;
	private List<String> mvnIds;
}
