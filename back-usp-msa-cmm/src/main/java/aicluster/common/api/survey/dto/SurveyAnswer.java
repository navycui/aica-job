package aicluster.common.api.survey.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyAnswer implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8656586398511102927L;
	private String answerId;
	private String shortAnswer;
}
