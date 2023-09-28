package aicluster.member.api.member.dto;

import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetListParam {

	private String loginId;
	private String memberNm;
	private String memberSt;
	private String memberType;
	private Date beginDay;
	private Date endDay;
	private Boolean instr;
	private String bizrno;

	public String getBeginDay() {
		if (this.beginDay == null) {
			return null;
		}
		return date.format(new Date(this.beginDay.getTime()), "yyyyMMdd");
	}

	public void setBeginDay(Date beginDay) {
		this.beginDay = null;
        if (beginDay != null) {
        	this.beginDay = new Date(beginDay.getTime());
        }
	}

	public String getEndDay() {
		if (this.endDay == null) {
			return null;
		}
		return date.format(new Date(this.endDay.getTime()), "yyyyMMdd");
	}

	public void setEndDay(Date endDay) {
		this.endDay = null;
        if (endDay != null) {
        	this.endDay = new Date(endDay.getTime());
        }
	}

}
