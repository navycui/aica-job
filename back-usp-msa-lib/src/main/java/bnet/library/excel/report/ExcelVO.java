package bnet.library.excel.report;

import java.io.Serializable;
import java.util.Date;

public class ExcelVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7249464161243463822L;
    private String loginId;
    private String userNm;
    private String deptNm;
    private Date hireDtime;
    private Long salary;

    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
    public String getDeptNm() {
        return deptNm;
    }
    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }
    public Date getHireDtime() {
    	if (this.hireDtime != null) {
			return new Date(this.hireDtime.getTime());
		}
		return null;
    }
    public void setHireDtime(Date hireDtime) {
    	this.hireDtime = null;
		if (hireDtime != null) {
			this.hireDtime = new Date(hireDtime.getTime());
		}
    }
    public Long getSalary() {
        return salary;
    }
    public void setSalary(Long salary) {
        this.salary = salary;
    }
	@Override
	public String toString() {
		return "ExcelVO [loginId=" + loginId + ", userNm=" + userNm + ", deptNm=" + deptNm + ", hireDtime=" + hireDtime
				+ ", salary=" + salary + "]";
	}
}
