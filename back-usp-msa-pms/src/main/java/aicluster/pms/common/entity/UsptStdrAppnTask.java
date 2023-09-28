package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.AppnTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrAppnTask extends AppnTask implements Serializable {
	private static final long serialVersionUID = 4225564494964773540L;
	@JsonIgnore
	private String stdrBsnsCd;		/** 기준사업코드 */
}
