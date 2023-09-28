package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlrsrcFninfRsrcDto implements Serializable {

	private static final long serialVersionUID = 8194831868456895080L;

	private String  rsrcId		;	       /** 자원ID     */
    private String  rsrcTypeNm	;	       /** 자원유형명 */

}
