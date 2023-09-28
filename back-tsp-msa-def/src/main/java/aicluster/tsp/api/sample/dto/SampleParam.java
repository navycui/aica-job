package aicluster.tsp.api.sample.dto;

import aicluster.tsp.common.sample.entity.CmmtCodeThree;
import aicluster.tsp.common.sample.entity.CmmtCodeTwo;
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
@ApiModel(description = "코드 샘플 테스트 DTO")
public class SampleParam implements Serializable {
	private static final long serialVersionUID = 1429070915955267092L;
	@ApiModelProperty(value = "샘플 엔티티 1")
	private CmmtCodeTwo cmmtCodeTwo;
	@ApiModelProperty(value = "샘플 엔티티 2")
	private CmmtCodeThree cmmtCodeThree;
}
