package aicluster.member.api.insider.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aicluster.member.common.entity.CmmtSysCharger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRecptnParam implements Serializable {

	private static final long serialVersionUID = -2008983251863419988L;

	private List<CmmtSysCharger> list;
	
	public List<CmmtSysCharger> getList() {
		List<CmmtSysCharger> list = new ArrayList<>();
		if(this.list != null) {
			list.addAll(this.list);
		}
		return list;
	}
	
	public void setList(List<CmmtSysCharger> list) {
		this.list = new ArrayList<>();
		if(list != null) {
			this.list.addAll(list);
		}
	}
}
