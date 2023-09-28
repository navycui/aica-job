package aicluster.common.api.board.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import aicluster.common.common.entity.CmmtBbs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3526017103726951456L;
	private CmmtBbs board;
	private HashMap<String, String> authority;
	
	public void setAuthority(Map<String, String> authority) {
		this.authority = new HashMap<>();
		if(authority != null) {
			this.authority = new HashMap<>(authority.size());
			for(String key:authority.keySet()){
			  this.authority.put(key, authority.get(key));
			}
		}
	}

	public Map<String, String> getAuthority() {
		Map<String, String> authority = new HashMap<>();
		for(String key:this.authority.keySet()){
			authority.put(key, this.authority.get(key));
		}
		return authority;
	}
}
