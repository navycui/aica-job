package aicluster.common.api.banner.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerGetListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -979254541695026799L;

	private String systemId;
	private String beginDay;
	private String endDay;
	private Boolean enabled;
	private String bannerNm;
}
