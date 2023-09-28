package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptRslt implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 5375873349984973603L;

	private String  rsltId           ;              /** 성과ID */
    private String  applyId          ;              /** 신청ID */
    private String  rsltSttusCd      ;              /** 성과상태코드(G:RSLT_STTUS) */
    private String  makeupReqCn      ;              /** 보완요청내용 */
    private String  attachmentGroupId;              /** 첨부파일그룹ID */
    @JsonIgnore
    private String  creatorId        ;              /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt        ;              /** 생성일시 */
    @JsonIgnore
    private String  updaterId        ;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt        ;              /** 수정일시 */

}
