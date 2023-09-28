package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.ApplyRealm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrApplyRealm extends ApplyRealm implements Serializable {
	private static final long serialVersionUID = -6827283397266601341L;
	@JsonIgnore
	private String stdrBsnsCd;		/** 기준사업코드 */
}
