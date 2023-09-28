package aicluster.pms.api.evl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.api.evl.dto.DspthResultDto;
import aicluster.pms.api.evl.dto.EvasResnCnDto;
import aicluster.pms.api.evl.dto.EvlCharmnOpinionDto;
import aicluster.pms.api.evl.dto.EvlResultConsentFormDto;
import aicluster.pms.api.evl.dto.EvlResultDspthParam;
import aicluster.pms.api.evl.dto.EvlResultDto;
import aicluster.pms.api.evl.dto.EvlTableDto;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.evl.dto.EvlTargetListParam;
import aicluster.pms.api.evl.dto.EvlTrgetIdParam;
import aicluster.pms.api.evl.dto.GnrlzEvlDto;
import aicluster.pms.api.evl.dto.PointListDto;
import aicluster.pms.api.evl.dto.PointListParam;
import aicluster.pms.api.evl.dto.PointParam;
import aicluster.pms.api.evl.service.EvlOnlineService;
import aicluster.pms.common.dto.EvlAtendListDto;
import aicluster.pms.common.dto.EvlResultListDto;
import aicluster.pms.common.dto.EvlSttusListDto;
import aicluster.pms.common.dto.EvlSttusMfcmmListDto;
import aicluster.pms.common.dto.EvlTargetListDto;
import aicluster.pms.common.entity.UsptEvlMfcmm;
import aicluster.pms.common.util.Code;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 평가진행관리 - 온라인 평가
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/evl-online")
public class EvlOnlineController {

	@Autowired
	private EvlOnlineService evlOnlineService;


	/**
	 * 평가진행 목록조회
	 * @param evlCmitExtrcTargetParam
	 * @param page
	 * @return
	 */
	@GetMapping("/target-list")
	public CorePagination<EvlTargetListDto> getEvlTargetList(EvlTargetListParam param, @RequestParam(defaultValue = "1") Long page) {
		return evlOnlineService.getEvlTargetList(param, page);
	}


	/**
	 * 평가진행 목록 엑셀 다운로드
	 * @param param
	 * @return
	 */
	@GetMapping("/target-list/excel-dwld")
	public ModelAndView getEvlTargetListExcel(EvlTargetListParam param){
		List<EvlTargetListDto> list = evlOnlineService.getEvlTargetListExcel(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가진행 관리 목록");
		ExcelSheet<EvlTargetListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("진행상태", "평가일시", "사업년도", "공고명", "평가위원회명", "평가유형", "평가단계", "분과", "모집유형", "접수차수", "간사", "평가예정일", "통보상태", "평가방법");
		sheet.setProperties("EvlSttusNm", "evlSttusDate", "bsnsYear", "pblancNm", "evlCmitNm", "evlTypeCd", "evlStepNm", "sectNm", "ordtmRcritNm", "rceptOdr", "orgnzrNm", "evlPrarnde", "dspthSttusNm", "evlMthNm");
		sheet.setTitle("평가진행 관리 목록");
		sheet.setSheetName("평가진행 관리 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 온라인평가 기본정보 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/basic")
	public EvlTargetListDto getEvlBasicInfo(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOnlineService.getEvlBasicInfo(evlCmitId);
	}


	/**
	 * 평가위원 출석표 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/atend")
	public JsonList<EvlAtendListDto> getEvlAtendList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}


	/**
	 * 위원장 설정
	 * @param evlCmitId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/charmn")
	public JsonList<EvlAtendListDto> modifyCharmn(@PathVariable("evlCmitId") String evlCmitId, @RequestBody UsptEvlMfcmm usptEvlMfcmm) {
		usptEvlMfcmm.setEvlCmitId(evlCmitId);
		evlOnlineService.modifyCharmn(usptEvlMfcmm);
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}


	/**
	 * 위원 불참 처리
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/nonatd")
	public JsonList<EvlAtendListDto> modifyAttendNonatd(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId) {
		UsptEvlMfcmm uemInfo = new UsptEvlMfcmm();
		uemInfo.setEvlCmitId(evlCmitId);
		uemInfo.setEvlMfcmmId(evlMfcmmId);
		uemInfo.setAtendSttusCd(Code.atendSttus.불참);
		evlOnlineService.modifyAtendSttus(uemInfo);
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}
	/**
	 * 위원 불참 취소 처리
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/nonatd-cancle")
	public JsonList<EvlAtendListDto> modifyAttendNonatdCancle(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId) {
		UsptEvlMfcmm uemInfo = new UsptEvlMfcmm();
		uemInfo.setEvlCmitId(evlCmitId);
		uemInfo.setEvlMfcmmId(evlMfcmmId);
		uemInfo.setAtendSttusCd(Code.atendSttus.불참);
		evlOnlineService.modifyAtendSttusCancel(uemInfo);
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}


	/**
	 * 위원 회피 처리
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/evas")
	public JsonList<EvlAtendListDto> modifyAttendEvas(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId, @RequestBody UsptEvlMfcmm uemInfo) {
		uemInfo.setEvlCmitId(evlCmitId);
		uemInfo.setEvlMfcmmId(evlMfcmmId);
		uemInfo.setAtendSttusCd(Code.atendSttus.회피);
		evlOnlineService.modifyAtendSttus(uemInfo);
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}
	/**
	 * 위원 회피 취소 처리
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/evas-cancle")
	public JsonList<EvlAtendListDto> modifyAttendEvasCancle(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId) {
		UsptEvlMfcmm uemInfo = new UsptEvlMfcmm();
		uemInfo.setEvlCmitId(evlCmitId);
		uemInfo.setEvlMfcmmId(evlMfcmmId);
		uemInfo.setAtendSttusCd(Code.atendSttus.회피);
		evlOnlineService.modifyAtendSttusCancel(uemInfo);
		return new JsonList<>(evlOnlineService.getEvlAtendList(evlCmitId));
	}


	/**
	 * 회피사유 조회
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/atend/{evlMfcmmId}/evas-resn")
	public EvasResnCnDto getEvasResnCn(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId) {
		UsptEvlMfcmm usptEvlMfcmm = new UsptEvlMfcmm();
		usptEvlMfcmm.setEvlCmitId(evlCmitId);
		usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);
		return evlOnlineService.getEvasResnCn(usptEvlMfcmm);
	}


	/**
	 *  회피사유 수정
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/evas-resn")
	public void modifyEvasResnCn(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId, @RequestBody UsptEvlMfcmm usptEvlMfcmm) {
		usptEvlMfcmm.setEvlCmitId(evlCmitId);
		usptEvlMfcmm.setEvlMfcmmId(evlMfcmmId);
		evlOnlineService.modifyEvasResnCn(usptEvlMfcmm);
	}


	/**
	 * 평가현황 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-sttus")
	public JsonList<EvlSttusListDto> getEvlSttusList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlOnlineService.getEvlSttusList(evlCmitId));
	}


	/**
	 * 가점부여 조회
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/add-point")
	public PointListDto getAddPointList(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, PointListParam param) {
		param.setEvlCmitId(evlCmitId);
		param.setEvlTrgetId(evlTrgetId);
		param.setEvlDivCd(Code.evlDiv.가점);
		return evlOnlineService.getPointList(param);
	}

	/**
	 * 가점부여 저장
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param param
	 */
	@PutMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/add-point")
	public void modifyAddPoint(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, @RequestBody PointParam param) {
		evlOnlineService.modifyPoint(evlCmitId, evlTrgetId, param);
	}


	/**
	 * 감정부여 조회
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/sub-point")
	public PointListDto getSubPointList(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, PointListParam param) {
		param.setEvlCmitId(evlCmitId);
		param.setEvlTrgetId(evlTrgetId);
		param.setEvlDivCd(Code.evlDiv.감점);
		return evlOnlineService.getPointList(param);
	}

	/**
	 * 감정부여 저장
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param param
	 */
	@PutMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/sub-point")
	public void modifySubPoint(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, @RequestBody PointParam param) {
		evlOnlineService.modifyPoint(evlCmitId, evlTrgetId, param);
	}


	/**
	 * 평가표 조회
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-table")
	public EvlTableDto getEvlTable(@PathVariable("evlCmitId") String evlCmitId, EvlTableParam param) {
		return evlOnlineService.getEvlTable(param);
	}


	/**
	 * 위원장 의견 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/charmn-opinion")
	public EvlCharmnOpinionDto getEvlCharmnOpinion(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOnlineService.getEvlCharmnOpinion(evlCmitId);
	}


	/**
	 * 평가결과 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result")
	public EvlResultDto getEvlResult(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOnlineService.getEvlResult(evlCmitId);
	}


	/**
	 * 평가결과 엑셀 다운로드
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/excel-dwld")
	public ModelAndView getEvlResultExcelDwld(@PathVariable("evlCmitId") String evlCmitId) {
		EvlResultDto resultDto = evlOnlineService.getEvlResult(evlCmitId);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가결과");
		ExcelSheet<String> sheet = new ExcelSheet<>();
		sheet.setSheetName("평가결과");
		sheet.setTitle("평가결과");

		if(resultDto != null && resultDto.getResultList() != null) {
			List<EvlResultListDto> resultList = resultDto.getResultList();

			List<EvlSttusMfcmmListDto> mfcmmList = null;

			if(resultList != null && !resultList.isEmpty()) {
				EvlResultListDto subDto = resultList.get(0);
				mfcmmList = subDto.getEvlSttusMfcmmList();
			}
			List<String> headerList = new ArrayList<String>();

			headerList.add("순위");
			headerList.add("평가결과");
			headerList.add("접수번호");
			headerList.add("사업자명/이름");
			headerList.add("최고점수");
			headerList.add("최저점수");
			headerList.add("총점");
			headerList.add("평균");
			headerList.add("가점");
			headerList.add("감점");
			headerList.add("종합점수");
			if(mfcmmList != null && !mfcmmList.isEmpty()) {
				for(EvlSttusMfcmmListDto mfcmmInfo : mfcmmList) {
					headerList.add(mfcmmInfo.getEvlMfcmmNm());
				}
			}

			String[] arrProperties = new String[headerList.size()];
			arrProperties[0] = "rn";
			arrProperties[1] = "slctnAtNm";
			arrProperties[2] = "receiptNo";
			arrProperties[3] = "memberNm";
			arrProperties[4] = "maxEvlScore";
			arrProperties[5] = "minEvlScore";
			arrProperties[6] = "sumEvlScore";
			arrProperties[7] = "avgEvlScore";
			arrProperties[8] = "addScore";
			arrProperties[9] = "subScore";
			arrProperties[10] = "totEvlScore";
			int propCnt = 11;
			int mfcmmCnt = 1;
			if(mfcmmList != null && !mfcmmList.isEmpty()) {
				for(EvlSttusMfcmmListDto mfcmmInfo : mfcmmList) {
					arrProperties[propCnt] = "evlMfcmm" + mfcmmCnt;
					propCnt++;
					mfcmmCnt++;
				}
			}
			sheet.setProperties(arrProperties);
			sheet.setHeaders(headerList.toArray());

			List rowList = new ArrayList();
			if(resultList != null) {
				for(EvlResultListDto data : resultList) {
					Map mapData = new HashMap();
					mapData.put("rn", data.getRn());
					mapData.put("slctnAtNm"		, data.getSlctnAtNm());
					mapData.put("receiptNo"		, data.getReceiptNo());
					mapData.put("memberNm"		, data.getMemberNm());
					mapData.put("maxEvlScore"	, data.getMaxEvlScore());
					mapData.put("minEvlScore"	, data.getMinEvlScore());
					mapData.put("sumEvlScore"	, data.getSumEvlScore());
					mapData.put("avgEvlScore"	, data.getAvgEvlScore());
					mapData.put("addScore"		, data.getAddScore());
					mapData.put("subScore"		, data.getSubScore());
					mapData.put("totEvlScore"	, data.getTotEvlScore());
					if(data.getEvlSttusMfcmmList() != null && !data.getEvlSttusMfcmmList().isEmpty()) {
						mfcmmCnt = 1;
						for(EvlSttusMfcmmListDto mfcmmInfo : data.getEvlSttusMfcmmList()) {
							mapData.put("evlMfcmm" + mfcmmCnt, mfcmmInfo.getEvlScore());
							mfcmmCnt++;
						}
					}
					rowList.add(mapData);
				}
			}
			sheet.addRows(rowList);
		} else {
			throw new InvalidationException("평가결과 정보가 없습니다.");
		}
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 선정 처리
	 * @param evlCmitId
	 * @param evlTrgetList
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-slctn")
	public void modifyEvlSlctn(@PathVariable("evlCmitId") String evlCmitId, @RequestBody List<EvlTrgetIdParam> evlTrgetList) {
		evlOnlineService.modifyEvlSlctn(evlCmitId, evlTrgetList, true);
	}


	/**
	 * 탈락 처리
	 * @param evlCmitId
	 * @param evlTrgetList
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-ptrmsn")
	public void modifyEvlPtrmsn(@PathVariable("evlCmitId") String evlCmitId, @RequestBody List<EvlTrgetIdParam> evlTrgetList) {
		evlOnlineService.modifyEvlSlctn(evlCmitId, evlTrgetList, false);
	}


	/**
	 * 자동선정 처리
	 * @param evlCmitId
	 */
	@PutMapping("/{evlCmitId}/evl-result/auto-evl-slctn")
	public void modifyAutoEvlSlctn(@PathVariable("evlCmitId") String evlCmitId) {
		evlOnlineService.modifyAutoEvlSlctn(evlCmitId);
	}


	/**
	 * 평가완료
	 * @param evlCmitId
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-compt")
	public void modifyEvlCompt(@PathVariable("evlCmitId") String evlCmitId) {
		evlOnlineService.modifyEvlCompt(evlCmitId);
	}


	/**
	 * 첨부파일 일괄다운로드
	 * @param response
	 * @param evlCmitId
	 */
	@GetMapping("/{evlCmitId}/evl-result/atchmnfl-dwld-all")
	public void downloaAllAtchmnfl(HttpServletResponse response, @PathVariable("evlCmitId") String evlCmitId) {
		evlOnlineService.downloaAllAtchmnfl(response, evlCmitId);
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param evlCmitId
	 * @param info
	 */
	@GetMapping("/{evlCmitId}/evl-result/atchmnfl-dwld")
	public void downloadAtchmnfl(HttpServletResponse response, @PathVariable("evlCmitId") String evlCmitId, CmmtAtchmnfl info) {
		evlOnlineService.downloadAtchmnfl(response, evlCmitId, info);
	}


	/**
	 * 첨부파일 삭제
	 * @param evlCmitId
	 * @param deleteList
	 */
	@PutMapping("/{evlCmitId}/evl-result/del-atchmnfl")
	public void delAtchmnfl(@PathVariable("evlCmitId") String evlCmitId,  @RequestBody List<CmmtAtchmnfl> deleteList) {
		evlOnlineService.delAtchmnfl(evlCmitId, deleteList);
	}


	/**
	 * 첨부파일 저장
	 * @param evlCmitId
	 * @param file
	 */
	@PutMapping("/{evlCmitId}/evl-result/add-atchmnfl")
	public void addAtchmnfl(@PathVariable("evlCmitId") String evlCmitId, @RequestPart(value = "file") MultipartFile file) {
		evlOnlineService.addAtchmnfl(evlCmitId, file);
	}


	/**
	 * 발송
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@PutMapping("/{evlCmitId}/evl-result/dspth")
	public DspthResultDto modifyEvlResultDspth(@PathVariable("evlCmitId") String evlCmitId, @RequestBody EvlResultDspthParam param) {
		return evlOnlineService.modifyEvlResultDspth(evlCmitId, param);
	}


	/**
	 * 종합평가결과 조회
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/{evlTrgetId}/gnrlz-evl")
	public GnrlzEvlDto getGnrlzEvl(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId) {
		return evlOnlineService.getGnrlzEvl(evlCmitId, evlTrgetId);
	}


	/**
	 * 평가결과 동의서 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/consent-form")
	public EvlResultConsentFormDto getEvlResultConsentForm(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOnlineService.getEvlResultConsentForm(evlCmitId);
	}

}
