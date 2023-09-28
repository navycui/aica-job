package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogtLoginLog implements Serializable {

	private static final long serialVersionUID = 6553017682673570767L;
	protected String logId;
	protected String apiSystemId;
	protected Date logDt;
	protected String memberId;
	protected String loginId;
	protected String memberNm;
	protected String memberType;
	protected String gender;
	protected Integer age;
	protected String memberIp;
	protected String positionNm;
	protected String deptNm;
	protected String pstinstNm;

	protected Long rowNum;
}
