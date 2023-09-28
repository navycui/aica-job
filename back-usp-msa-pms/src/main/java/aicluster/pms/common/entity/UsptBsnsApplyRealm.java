package aicluster.pms.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.api.bsns.dto.ApplyRealm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsApplyRealm extends ApplyRealm implements Serializable {
	private static final long serialVersionUID = -3692104685687970238L;
	@JsonIgnore
	private String bsnsCd;		/** 사업코드 */
}
