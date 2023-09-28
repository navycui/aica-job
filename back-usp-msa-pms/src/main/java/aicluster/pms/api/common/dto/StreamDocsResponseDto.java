package aicluster.pms.api.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class StreamDocsResponseDto implements Serializable {
	private static final long serialVersionUID = 1890277101288665867L;
	/** 등록된 문서를 열람할 수 있는 전체 URL 값 */
	private String alink;
	@JsonIgnore
	private String category;
	@JsonIgnore
	private String createAt;
	@JsonIgnore
	private String updatedAt;
	/** 문서를 다운로드 할 때 설정되는 파일명 */
	@JsonIgnore
	private String givenName;
	/** 관리자 페이지의 문서목록에서 표시되는 문서명 */
	private String docName;
	@JsonIgnore
	private int fileSize;
	@JsonIgnore
	private boolean deleted;
	@JsonIgnore
	private String description;
	@JsonIgnore
	private boolean crypted;
	@JsonIgnore
	private boolean hasPassword;
	@JsonIgnore
	private String type;
	@JsonIgnore
	private boolean originExists;
	@JsonIgnore
	private String streamdocsId;
	@JsonIgnore
	private String secureId;
}
