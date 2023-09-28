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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.api.evl.dto.DspthResultDto;
import aicluster.pms.api.evl.dto.EvasResnCnDto;
import aicluster.pms.api.evl.dto.EvlCharmnOpinionDto;
import aicluster.pms.api.evl.dto.EvlOfflineTableDto;
import aicluster.pms.api.evl.dto.EvlResultConsentFormDto;
import aicluster.pms.api.evl.dto.EvlResultDspthParam;
import aicluster.pms.api.evl.dto.EvlResultDto;
import aicluster.pms.api.evl.dto.EvlTableDto;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.evl.dto.EvlTrgetIdParam;
import aicluster.pms.api.evl.dto.GnrlzEvlDto;
import aicluster.pms.api.evl.dto.PointListDto;
import aicluster.pms.api.evl.dto.PointListParam;
import aicluster.pms.api.evl.dto.PointParam;
import aicluster.pms.api.evl.service.EvlOfflineService;
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
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 평가진행관리 - 오프라인 평가
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/evl-offline")
public class EvlOfflineController {

	@Autowired
	private EvlOfflineService evlOfflineService;

	/**
	 * 오프라인평가 기본정보 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/basic")
	public EvlTargetListDto getEvlBasicInfo(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOfflineService.getEvlBasicInfo(evlCmitId);
	}


	/**
	 * 평가위원 출석표 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/atend")
	public JsonList<EvlAtendListDto> getEvlAtendList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		evlOfflineService.modifyCharmn(usptEvlMfcmm);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
	}

	/**
	 * 위원 출석	처리
	 * @param evlCmitId
	 * @param evlMfcmmId
	 * @param usptEvlMfcmm
	 * @return
	 */
	@PutMapping("/{evlCmitId}/atend/{evlMfcmmId}/atend")
	public JsonList<EvlAtendListDto> modifyAttend(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlMfcmmId") String evlMfcmmId) {
		UsptEvlMfcmm uemInfo = new UsptEvlMfcmm();
		uemInfo.setEvlCmitId(evlCmitId);
		uemInfo.setEvlMfcmmId(evlMfcmmId);
		uemInfo.setAtendSttusCd(Code.atendSttus.출석);
		evlOfflineService.modifyAtendSttus(uemInfo);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		evlOfflineService.modifyAtendSttus(uemInfo);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		evlOfflineService.modifyAtendSttusCancel(uemInfo);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		evlOfflineService.modifyAtendSttus(uemInfo);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		evlOfflineService.modifyAtendSttusCancel(uemInfo);
		return new JsonList<>(evlOfflineService.getEvlAtendList(evlCmitId));
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
		return evlOfflineService.getEvasResnCn(usptEvlMfcmm);
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
		evlOfflineService.modifyEvasResnCn(usptEvlMfcmm);
	}


	/**
	 * 평가현황 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-sttus")
	public JsonList<EvlSttusListDto> getEvlSttusList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlOfflineService.getEvlSttusList(evlCmitId));
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
		return evlOfflineService.getPointList(param);
	}

	/**
	 * 가점부여 저장
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param param
	 */
	@PutMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/add-point")
	public void modifyAddPoint(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, @RequestBody PointParam param) {
		evlOfflineService.modifyPoint(evlCmitId, evlTrgetId, param);
	}


	/**
	 * 감점부여 조회
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/sub-point")
	public PointListDto getSubPointList(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, PointListParam param) {
		param.setEvlCmitId(evlCmitId);
		param.setEvlTrgetId(evlTrgetId);
		param.setEvlDivCd(Code.evlDiv.감점);
		return evlOfflineService.getPointList(param);
	}

	/**
	 * 감점부여 저장
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @param param
	 */
	@PutMapping("/{evlCmitId}/evl-sttus/{evlTrgetId}/sub-point")
	public void modifySubPoint(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId, @RequestBody PointParam param) {
		evlOfflineService.modifyPoint(evlCmitId, evlTrgetId, param);
	}

	/**
	 * 평가표 다운로드
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-table/dwld")
	public JsonList<EvlTableDto> getDownEvlTableList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlOfflineService.getDownEvlTableList(evlCmitId));
	}

	/**
	 * 평가표 조회
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-table")
	public EvlTableDto getEvlTable(@PathVariable("evlCmitId") String evlCmitId, EvlTableParam param) {
		return evlOfflineService.getEvlTable(param);
	}

	/**
	 * 평가표 등록
	 * @param evl_mfcmm_id, evlTrgetId evl_iem_id
	 * @return
	 */
	@PutMapping("/{evlCmitId}/evl-table/save")
	public void modifyEvlTable(@PathVariable("evlCmitId") String evlCmitId, @RequestBody EvlOfflineTableDto evlOfflineTableDto){
		log.debug("#####	modifyEvlTable : {}", evlOfflineTableDto);
		evlOfflineService.modifyEvlTable(evlOfflineTableDto);
	}


	/**
	 * 위원장 의견 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/charmn-opinion")
	public EvlCharmnOpinionDto getEvlCharmnOpinion(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOfflineService.getEvlCharmnOpinion(evlCmitId);
	}

	/**
	 * 위원장 의견 등록
	 * @param evlCmitId ,evlCharmnOpinionCn
	 * @return
	 */
	@PutMapping("/{evlCmitId}/charmn-opinion/save")
	public void modifyCharmnOpinionCn(@PathVariable("evlCmitId") String evlCmitId, @RequestBody(required = false) EvlCharmnOpinionDto evlCharmnOpinionDto){
		log.debug("#####	modifyCharmnOpinionCn : {}", evlCharmnOpinionDto);
		evlOfflineService.modifyCharmnOpinionCn(evlCmitId, evlCharmnOpinionDto);
	}


	/**
	 * 평가결과 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result")
	public EvlResultDto getEvlResult(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOfflineService.getEvlResult(evlCmitId);
	}


	/**
	 * 평가결과 엑셀 다운로드
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/excel-dwld")
	public ModelAndView getEvlResultExcelDwld(@PathVariable("evlCmitId") String evlCmitId) {
		EvlResultDto resultDto = evlOfflineService.getEvlResult(evlCmitId);

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
		evlOfflineService.modifyEvlSlctn(evlCmitId, evlTrgetList, true);
	}


	/**
	 * 탈락 처리
	 * @param evlCmitId
	 * @param evlTrgetList
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-ptrmsn")
	public void modifyEvlPtrmsn(@PathVariable("evlCmitId") String evlCmitId, @RequestBody List<EvlTrgetIdParam> evlTrgetList) {
		evlOfflineService.modifyEvlSlctn(evlCmitId, evlTrgetList, false);
	}


	/**
	 * 자동선정 처리
	 * @param evlCmitId
	 */
	@PutMapping("/{evlCmitId}/evl-result/auto-evl-slctn")
	public void modifyAutoEvlSlctn(@PathVariable("evlCmitId") String evlCmitId) {
		evlOfflineService.modifyAutoEvlSlctn(evlCmitId);
	}


	/**
	 * 평가완료
	 * @param evlCmitId
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-compt")
	public void modifyEvlCompt(@PathVariable("evlCmitId") String evlCmitId) {
		evlOfflineService.modifyEvlCompt(evlCmitId);
	}

	/**
	 * 평가결과초기화
	 * @param evlCmitId
	 */
	@PutMapping("/{evlCmitId}/evl-result/evl-init")
	public void modifyEvlComptInit(@PathVariable("evlCmitId") String evlCmitId) {
		evlOfflineService.modifyEvlComptInit(evlCmitId);
	}


	/**
	 * 첨부파일 일괄다운로드
	 * @param response
	 * @param evlCmitId
	 */
	@GetMapping("/{evlCmitId}/evl-result/atchmnfl-dwld-all")
	public void downloaAllAtchmnfl(HttpServletResponse response, @PathVariable("evlCmitId") String evlCmitId) {
		evlOfflineService.downloaAllAtchmnfl(response, evlCmitId);
	}


	/**
	 * 첨부파일 다운로드
	 * @param response
	 * @param evlCmitId
	 * @param info
	 */
	@GetMapping("/{evlCmitId}/evl-result/atchmnfl-dwld")
	public void downloadAtchmnfl(HttpServletResponse response, @PathVariable("evlCmitId") String evlCmitId, CmmtAtchmnfl info) {
		evlOfflineService.downloadAtchmnfl(response, evlCmitId, info);
	}


	/**
	 * 첨부파일 삭제
	 * @param evlCmitId
	 * @param deleteList
	 */
	@PutMapping("/{evlCmitId}/evl-result/del-atchmnfl")
	public void delAtchmnfl(@PathVariable("evlCmitId") String evlCmitId,  @RequestBody List<CmmtAtchmnfl> deleteList) {
		evlOfflineService.delAtchmnfl(evlCmitId, deleteList);
	}


	/**
	 * 첨부파일 저장
	 * @param evlCmitId
	 * @param file
	 */
	@PutMapping("/{evlCmitId}/evl-result/add-atchmnfl")
	public void addAtchmnfl(@PathVariable("evlCmitId") String evlCmitId, @RequestPart(value = "file") MultipartFile file) {
		evlOfflineService.addAtchmnfl(evlCmitId, file);
	}


	/**
	 * 통보
	 * @param evlCmitId
	 * @param param
	 * @return
	 */
	@PutMapping("/{evlCmitId}/evl-result/dspth")
	public DspthResultDto modifyEvlResultDspth(@PathVariable("evlCmitId") String evlCmitId, @RequestBody EvlResultDspthParam param) {
		return evlOfflineService.modifyEvlResultDspth(evlCmitId, param);
	}


	/**
	 * 종합평가결과 조회
	 * @param evlCmitId
	 * @param evlTrgetId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/{evlTrgetId}/gnrlz-evl")
	public GnrlzEvlDto getGnrlzEvl(@PathVariable("evlCmitId") String evlCmitId, @PathVariable("evlTrgetId") String evlTrgetId) {
		return evlOfflineService.getGnrlzEvl(evlCmitId, evlTrgetId);
	}


	/**
	 * 평가결과 동의서 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-result/consent-form")
	public EvlResultConsentFormDto getEvlResultConsentForm(@PathVariable("evlCmitId") String evlCmitId) {
		return evlOfflineService.getEvlResultConsentForm(evlCmitId);
	}

}
