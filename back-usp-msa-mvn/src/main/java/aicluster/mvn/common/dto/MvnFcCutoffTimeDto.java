package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcCutoffTimeDto implements Serializable {

    private static final long serialVersionUID = -5397065570558885893L;

	private String  mvnFcId;			   /** 입주시설ID */
    private String  ymd;			       /** 일자       */

    private List<CutoffTimeDto> cutoffTimeList;     /** 차단시간목록 */

}
