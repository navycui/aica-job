package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsptResrceAsgnDstb implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1451156260741512444L;

	private String  alrsrcId   ;                   /** 자원할당ID */
	private String  rsrcId     ;                   /** 자원ID */
	private String  rsrcGroupCd;
	private String  rsrcGroupNm;
	private String  rsrcTypeNm ;
	private Boolean rsrcUseYn  ;                   /** 자원사용여부 */
	private Integer rsrcDstbQy ;                   /** 자원배분량 */
	private String  rsrcDstbCn ;                   /** 자원배분내용 */
	@JsonIgnore
	private String  creatorId  ;                   /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt  ;                   /** 생성일시 */
	@JsonIgnore
	private String  updaterId  ;                   /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt  ;                   /** 수정일시 */

}
