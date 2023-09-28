package aicluster.tsp.common.sample.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "코드 샘플 테스트 엔티티2")
public class CmmtCodeThree implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7526508155923670334L;
	@ApiModelProperty(value = "코드 그룹")
	private String codeGroup;
	@ApiModelProperty(value = "코드")
	private String code;
}
