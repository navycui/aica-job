package aicluster.tsp.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "도커 템플릿 Dto")
public class DockerContainerTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6872834749696672654L;

	@ApiModelProperty(value = "템플릿ID")
	private String Id;
	@ApiModelProperty(value = "템플릿 타입")
	private String templateType;
	@ApiModelProperty(value = "최대 갯수")
	private Integer maxCount;
	@ApiModelProperty(value = "인스턴스당 최대 갯수")
	private Integer instanceMaxCount;
	@ApiModelProperty(value = "cpu")
	private Integer cpu;
	@ApiModelProperty(value = "메모리")
	private Integer memory;
	@ApiModelProperty(value = "GPU 메모리")
	private Integer gpuMemory;
	@ApiModelProperty(value = "NAS타입")
	private String nasType;
	@ApiModelProperty(value = "HPE NAS 타입")
	private String hpeNasType;
	@ApiModelProperty(value = "설명")
	private String description;
}
