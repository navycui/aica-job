package aicluster.tsp.api.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "공통코드")
public class CommonCodeParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;
	@ApiModelProperty(value = "공통코드")
	private HashMap<String, CommonCodeGroup> codeGroup;


	public void setCodeGroup(HashMap<String, CommonCodeGroup> codeGroup) {
		if (codeGroup != null) {
			this.codeGroup = new HashMap<>(codeGroup.size());
			for (String key : codeGroup.keySet()) {
				this.codeGroup.put(key, codeGroup.get(key));
			}
		} else {
			this.codeGroup = new HashMap<>();
		}
	}
	public HashMap<String, CommonCodeGroup> getCodeGroup() {
		HashMap<String, CommonCodeGroup> codeGroup = new HashMap<>();
		if (this.codeGroup != null) {
			for (String key : this.codeGroup.keySet()) {
				codeGroup.put(key, this.codeGroup.get(key));
			}
		}
		return codeGroup;
	}
}