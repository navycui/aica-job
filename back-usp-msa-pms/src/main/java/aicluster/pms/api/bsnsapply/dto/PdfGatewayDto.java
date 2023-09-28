package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PdfGatewayDto implements Serializable {
	private static final long serialVersionUID = -8311722128415369506L;
	/**
	 * 요청된 작업을 내부적으로 구분하는 유일키값을 가집니다.
	 * 정보 차원으로 제공되며 사용자가 사용하는 값은 아닙니다.
	 */
	private String id;
	/**
	 * 요청된 작업을 구분할 수 있는 유일키값을 가집니다.
	 * 요청된 작업의 상태조회, 결과파일 다운로드, 오류정보 조회 시 사용되는 값이므로 따로 저장하여 관리하여야 합니다.
	 */
	private String oid;
	/**
	 * 항상 null 값을 가집니다.
	 * Client Java API 를 사용하여 변환작업 요청 시 null 이외의 값을 가질 수 있습니다
	 */
	private String externalId;
	/**
	 * 작업 요청 시 설정한 작업의 이름을 가집니다
	 */
	private String name;
	/**
	 * 요청된 작업의 상태값을 가집니다.
	 *  SUCCESS : 변환작업에 성공했을 때 갖는 값입니다.
	 *  FAILURE : 변환작업에 실패했을 때 갖는 값입니다.
	 */
	private String status;
	/**
	 * 요청된 작업이 생성된 시간을 밀리초 단위로 가집니다.
	 */
	private Long createdAt;
	/**
	 * 요청된 작업이 대기상태로 변경된 시간을 밀리초 단위로 가집니다.
	 */
	private Long queuedAt;
	/**
	 * 요청된 작업의 변환이 시작된 시간을 밀리초 단위로 가집니다.
	 */
	private Long startedAt;
	/**
	 * 요청된 작업의 처리가 완료된 시간을 밀리초 단위로 가집니다.
	 * 일반적으로 PDF Gateway 서버에서 작업이 완전히 종료된 시간을 갖습니다.
	 */
	private Long completedAt;
	/**
	 * 요청된 작업의 상태가 마지막에 변경된 시간을 밀리초 단위로 가집니다
	 */
	private Long updatedAt;
	/**
	 * 변환작업 시 오류가 발생할 경우 재변환을 시도하는 최대값을 가집니다
	 */
	private Long maxRetryCount;
	/**
	 * 요청된 작업을 변환하는 중 재변환을 시도한 횟수를 가집니다.
	 */
	private Long retryCount;
	/**
	 * 항상 “api” 값을 가집니다
	 */
	private String collectedBy;
	/**
	 * 작업 요청 시 설정한 callbackUri 값을 가집니다.
	 */
	private String callbackUri;
}
