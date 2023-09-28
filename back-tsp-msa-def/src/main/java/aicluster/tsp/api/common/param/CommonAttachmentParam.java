package aicluster.tsp.api.common.param;

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
@ApiModel(description = "첨부파일 정보")
public class CommonAttachmentParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;
	@ApiModelProperty(value = "첨부파일ID")
	private String attachmentId;
	@ApiModelProperty(value = "첨부파일 이름")
	private String fileNm;
	@ApiModelProperty(value = "첨부파일 용량")
	private Integer fileSize;
	@ApiModelProperty(value = "첨부파일 타입")
	private String contentType;
}