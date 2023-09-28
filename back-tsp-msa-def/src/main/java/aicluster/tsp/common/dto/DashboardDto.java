package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@ApiModel(description = "대시보드에 사용되는 카운트 정보")
public class DashboardDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8250845185087210065L;
	@ApiModelProperty(value = "견적신청")
	private Integer estimateCount;				/** 견적신청 */

	@ApiModelProperty(value = "사용신청")
	private Integer useCount;					/** 사용신청 */

	@ApiModelProperty(value = "장비사용 신청관리")
	private Integer rentalTotal;				/** 장비사용 신청관리 */

	@ApiModelProperty(value = "기간연장신청")
	private Integer extentionCount;			/** 기간연장신청 */

	@ApiModelProperty(value = "결과보고제출")
	private Integer reportCount;				/** 결과보고제출 */

	@ApiModelProperty(value = "장비사용관리")
	private Integer usageTotal;				/** 장비사용관리 */

	@ApiModelProperty(value = "사용신청")
	private Integer resourceCount;				/** 사용신청 */

	@ApiModelProperty(value = "반환요청")
	private Integer resourceReturnCount;		/** 반환요청 */

	@ApiModelProperty(value = "실증자원사용관리")
	private Integer resourceTotal;				/** 실증자원사용관리 */
}
