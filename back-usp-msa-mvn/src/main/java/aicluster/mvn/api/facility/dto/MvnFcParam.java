package aicluster.mvn.api.facility.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnFcParam implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 1504358636194593309L;
	private String mvnFcId;
    private String mvnFcNm;
    private String mvnFcType;
    private String mvnFcDtype;
    private String mvnFcCn;
    private String bnoCd;
    private String roomNo;
    private Long mvnFcCapacity;
    private String mvnFcar;
    private String reserveType;
    private Boolean hr24Yn;
    private String utztnBeginHh;
    private String utztnEndHh;
    private String mainFc;
    private String imageAltCn;

    private List<MvnFcRsvtDdParam> mvnFcRsvtDdList;
}
