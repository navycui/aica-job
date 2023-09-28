package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvlCmitExtrcTargetParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1138981089368004610L;

	String  bsnsYear;                       /** 사업년도 */
	String  pblancNm;                       /** 공고명 */
	String  evlCmitNm;                      /** 평가위원회명 */
	String  sectNm;                         /** 분과명 */
	String  evlStepNm;                      /** 평가단계명 */
	String  orgnzrNm;                       /** 간사명(CMMT_INSIDER.MEMBER_ID) */
	String  evlSttusCd;                     /** 평가상태코드(G:EVL_ST) */
	String  evlTypeCd;          			/** 평가유형코드(G:EVL_TYPE) */
	Boolean dspth;                          /** 통보여부 */
	Boolean online;                         /** 온라인여부 */

	/** 엑셀 다운로드 여부 */
	@JsonIgnore
	private boolean isExcel;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

//
//	@JsonIgnore
//	String  flag; 							/** 변경 플래그(I,U,D) */
//	String  evlCmitId;                      /** 평가위원회ID */
//	@JsonIgnore
//	String  sectId;                         /** 분과ID */
//
//	@JsonIgnore
//	String  evlStepId;                      /** 평가단계ID */
//
//	@JsonIgnore
//	String  evlTableId;                     /** 평가표ID */
//	Integer mfcmmCo;                        /** 위원수 */
//	String  evlPrarnde;                     /** 평가예정일 */
//	@JsonIgnore
//	String  beginHour;                      /** 시작시간 */
//	@JsonIgnore
//	String  endHour;                        /** 종료시간 */
//	String  evlPlace;                       /** 평가장소 */
//	@JsonIgnore
//	String  orgnzrId;                       /** 간사ID(CMMT_INSIDER.MEMBER_ID) */
//
//	@JsonIgnore
//	String  evlCharmnOpinionCn;             /** 평가위원장종합의견내용 */
//	@JsonIgnore
//
//	String  evlSttusNm;                     /** 평가상태명(G:EVL_ST) */
//	Date    evlSttusDt;                     /** 평가상태일시 */
//
//	String  dspthNm;                        /** 통보여부명 */
//	Date    dspthDt;                        /** 통보일시 */
//
//	String  attachmentGroupId;              /** 첨부파일그룹ID */
//
//	String  ordtmRcritNm;                    /** 상시모집명 */
//
//	String  evlTypeNm;          			 /** 평가유형명(G:EVL_TYPE) */
//	String  processSttusNm;					 /** 처리상태명 */
//	String  rceptOdr;						 /** 접수차수 */
//
//	String  evlMthdCd;						 /** 평가방식코드 */
//	String  bsnsNm; 						 /** 사업명 */
//
//	int waitCo;								 	/** 대기건수 */
//	int absnceCo;							 	/** 부재건수 */
//	int rejectCo;								/** 거부건수 */
//	int confmCo;								/** 승낙건수 */
//	int evasCo;									/** 회피건수 */


//	private int rn;							 /** 순번 */



//	@JsonIgnore
//	private String creatorId;		/** 생성자 ID */
//	@JsonIgnore
//	private Date createdDt;			/** 생성일시 */
//	@JsonIgnore
//	private String updaterId;		/** 수정자 ID */
//	@JsonIgnore
//	private Date updatedDt;			/** 수정일시 */
}
