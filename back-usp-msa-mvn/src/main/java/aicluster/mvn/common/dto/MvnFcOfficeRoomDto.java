package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcOfficeRoomDto implements Serializable {

    private static final long serialVersionUID = 1422878847015357249L;

	private String  bnoRoomNo;                      /** 입주호실 코드 */
    private String  bnoRoomNm;                      /** 입주호실 명칭 */
}
