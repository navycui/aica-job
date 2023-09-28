package aicluster.tsp.common.dto;

import aicluster.tsp.api.common.param.CommonAttachmentParam;
import bnet.library.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "신청자 정보 DTO")
public class ApplcntDto implements Serializable {

	private static final long serialVersionUID = 8126902709889699942L;
//	@ApiModelProperty(value = "신청ID자")
//	private String applcntId;
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
	@ApiModelProperty(value = "주소")
	private String adres;

	@ApiModelProperty(value = "사업자등록파일Id")
	private String bsnlcnsFileId;
	@ApiModelProperty(value = "사업자등록파일 정보")
	private CommonAttachmentParam bsnlcnsFile;
}