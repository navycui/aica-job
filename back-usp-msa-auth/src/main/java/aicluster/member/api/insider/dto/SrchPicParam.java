package aicluster.member.api.insider.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SrchPicParam implements Serializable {

	private static final long serialVersionUID = 7715103668800422850L;
	
	private String deptNm;
	private String loginId;
	private String memberNm;
}
