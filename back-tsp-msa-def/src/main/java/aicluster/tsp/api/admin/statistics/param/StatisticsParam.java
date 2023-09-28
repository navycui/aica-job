package aicluster.tsp.api.admin.statistics.param;

import aicluster.tsp.common.dto.StatisticsDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "통계")
public class StatisticsParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;

	@ApiModelProperty(value = "검색 시작일")
	private Date beginDt;
	@ApiModelProperty(value = "검색 종료일")
	private Date endDt;

	@ApiModelProperty(value = "통계 결과 리스트", hidden = true)
	private List<StatisticsDto> statistics = new ArrayList<>();

	@ApiModelProperty(value = "사용기업 수", hidden = true)
	private Integer entrprsCount;


	public StatisticsParam(List<StatisticsDto> data){
		statistics = data;
	}


	public void addStatistics(StatisticsDto data){
		statistics.add(data);
	}

	public Date getBeginDt() {
		if (this.beginDt != null) {
			return new Date(this.beginDt.getTime());
		}
		return null;
	}
	public void setBeginDt(Date beginDt) {
		this.beginDt = null;
		if (beginDt != null) {
			this.beginDt = new Date(beginDt.getTime());
		}
	}
	public Date getEndDt() {
		if (this.endDt != null) {
			return new Date(this.endDt.getTime());
		}
		return null;
	}
	public void setEndDt(Date endDt) {
		this.endDt = null;
		if (endDt != null) {
			this.endDt = new Date(endDt.getTime());
		}
	}

}