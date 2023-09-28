package aicluster.member.api.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeGroupDto {

	private String codeGroup;
	private String codeGroupNm;
	private String remark;
	private Boolean enabled;

}
