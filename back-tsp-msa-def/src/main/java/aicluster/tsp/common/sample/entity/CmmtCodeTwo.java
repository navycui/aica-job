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
@ApiModel(description = "코드 샘플 테스트 엔티티1")
public class CmmtCodeTwo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6221527950989774531L;
	@ApiModelProperty(value = "코드 이름")
	private String codeNm;
	@ApiModelProperty(value = "리마크")
	private String remark;
}
