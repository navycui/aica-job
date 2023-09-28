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
public class MvnFcRsvtListParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 780269359268863846L;
	private String reserveSt;		/** 예약상태(G:RESERVE_ST) */
	private String mvnFcDtype;		/** 입주시설상세유형(G:MVN_FC_DTYPE) */
	private String reserveType;		/** 예약유형(G:RESERVE_TYPE) */
	private String rsvtBeginDay;	/** 예약조회기간(시작일) */
	private String rsvtEndDay;		/** 예약조회기간(종료일) */
	private Boolean mvnYn;       	/** 입주여부(null:전체, true:입주, false:미입주) */
	private String searchType;		/** 검색구분(FC_NM:시설명, MEMBER_NM:사업자명) */
	private String searchCn;		/** 검색내용 */
	private String mvnFcNm;			/** 시설명 */
	private String memberNm;		/** 회원명 */
}
