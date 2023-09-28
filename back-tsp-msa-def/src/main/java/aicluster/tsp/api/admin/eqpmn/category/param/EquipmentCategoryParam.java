package aicluster.tsp.api.admin.eqpmn.category.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentCategoryParam implements Serializable {
	private static final long serialVersionUID = 7578176174815789055L;

	private String eqpmnClId;
	private String eqpmnClNm;
	private String eqpmnLclasId;
	private Integer ordr;
	private boolean useAt;

	private Integer level;
	private String path;
}
