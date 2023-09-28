package aicluster.mvn.common.dto;

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
public class AlrsrcStatusSmmryDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 4178887350446301936L;

	private Long    useCmpnyCnt;			   /** 이용중인 업체 수 */

    private List<AlrsrcFninfStatusDto> alrsrcFninfSttusList;  /** USPT_ALRSRC_DSTB, USPT_ALRSRC_CMPNY, USPT_ALRSRC_FNINF 참조하여 생성한 자원별 현황 목록 */

}
