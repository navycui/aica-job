package aicluster.tsp.api.common.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "신청자 정보")
public class CommonApplcntParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;
	@ApiModelProperty(value = "신청자 Id", hidden = true)
	private String applcntId;
	@ApiModelProperty(value = "구분")
	private String mberDiv;
	@ApiModelProperty(value = "사업자명/이름")
	private String entrprsNm;
	@ApiModelProperty(value = "신청자 이름")
	private String userNm;
	@ApiModelProperty(value = "직위")
	private String ofcps;
	@ApiModelProperty(value = "연락처")
	private String cttpc;
	@ApiModelProperty(value = "이메일")
	private String email;
	@ApiModelProperty(value = "AI직접단지 참여사업 참여 여부")
	private String partcptnDiv;

	@JsonIgnore
	@ApiModelProperty(value = "사업자등록파일ID")
	private String bsnlcnsFileId;
	@ApiModelProperty(value = "개인정보수집동의여부")
	private Boolean indvdlinfoColctAgreAt;
//	@ApiModelProperty(value = "생성자ID")
//	private String creatrId;
//	@ApiModelProperty(value = "수정자ID")
//	private String updusrId;

}