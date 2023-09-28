package aicluster.mvn.api.reservation.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRsvtListParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1294886674600347730L;
	private String srchBeginDay;	/** 조회 시작일 */
	private String srchEndDay;		/** 조회 종료일 */
	private String reserveSt;		/** 예약상태(G: RESERVE_ST) */
	private String mvnFcNm;			/** 시설명 */
}
