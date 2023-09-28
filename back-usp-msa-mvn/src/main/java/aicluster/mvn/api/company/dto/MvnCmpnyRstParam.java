package aicluster.mvn.api.company.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MvnCmpnyRstParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2665953306285638411L;

	@JsonIgnore
	private String mvnId;
	@JsonIgnore
	private String sbmsnYm;
	private String rsltId;
}
