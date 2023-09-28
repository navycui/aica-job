package aicluster.framework.log.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginLog implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
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
}
