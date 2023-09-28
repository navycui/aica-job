package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CutoffTimeDto implements Serializable {

    private static final long serialVersionUID = -4816142238085218781L;

	private String  time;			       /** 시간(HHmm)        */
}
