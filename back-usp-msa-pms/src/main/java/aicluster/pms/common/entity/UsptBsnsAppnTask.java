package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.AppnTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsAppnTask extends AppnTask implements Serializable{
	private static final long serialVersionUID = 3963266188812158593L;
	@JsonIgnore
	private String bsnsCd;			/** 사업코드 */
}
