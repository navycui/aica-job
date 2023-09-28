package aicluster.pms.api.rslt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.rslt.dto.PresentnReqParam;
import aicluster.pms.api.rslt.dto.RsltDto;
import aicluster.pms.api.rslt.dto.RsltIdDto;
import aicluster.pms.api.rslt.dto.RsltIdxParam;
import aicluster.pms.api.rslt.dto.RsltListParam;
import aicluster.pms.api.rslt.dto.RsltMakeupParam;
import aicluster.pms.api.rslt.dto.RsltSaveBeforeDto;
import aicluster.pms.api.rslt.dto.RsltStatsDto;
import aicluster.pms.api.rslt.dto.RsltStatsParam;
import aicluster.pms.api.rslt.service.RsltService;
import aicluster.pms.common.dto.RsltListDto;
import aicluster.pms.common.entity.UsptRsltHist;
import aicluster.pms.common.util.Code;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 성과관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/rslt")
public class RsltController {

	@Autowired
	private RsltService rsltService;

	/**
	 * 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<RsltListDto> getList(RsltListParam param, @RequestParam(defaultValue = "1") Long page) {
		return rsltService.getList(param, page);
	}

	/**
	 * 목록 엑셀 저장
	 * @param param
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getExcelDwld(RsltListParam param){
		List<RsltListDto> list = rsltService.getListExcelDwld(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("성과관리");
		ExcelSheet<RsltListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("처리상태", "사업연도" , "사업명", "접수번호", "과제명", "회원명", "제출일");
		sheet.setProperties("rsltSttus", "bsnsYear", "bsnsNm", "receiptNo", "taskNm",  "memberNm", "presentnDt");
		sheet.setTitle("성과관리 목록");
		sheet.setSheetName("성과관리");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 성과 일괄 제출요청
	 * @param param
	 * @return
	 */
	@PostMapping("/presentn-req")
	public CorePagination<RsltListDto> addPresentnReq(@RequestBody PresentnReqParam param) {
		rsltService.addPresentnReq(param);
		RsltListParam listParam = new RsltListParam();
		return rsltService.getList(listParam, null);
	}

	/**
	 * 제출과제 목록 조회
	 * @param param
	 * @param page
	 * @return
	 */
	@GetMapping("/presentn-task")
	public CorePagination<RsltListDto> getPresentnTaskList(RsltListParam param, @RequestParam(defaultValue = "1") Long page) {
		param.setIsPresentnTask("Y");
		return rsltService.getList(param, page);
	}

	/**
	 * 성과 등록 전 정보 조회
	 * @param applyId
	 * @return
	 */
	@GetMapping("/save/before/{applyId}")
	public RsltSaveBeforeDto getSaveBefore(@PathVariable("applyId") String applyId) {
		return rsltService.getSaveBefore(applyId);
	}

	/**
	 * 성과 등록
	 * @param applyId
	 * @param infoList
	 * @param rsltIdxFileList
	 * @param attachFileList
	 * @return
	 */
	@PostMapping("/save/{applyId}")
	public RsltIdDto add(@PathVariable("applyId") String applyId
					, @RequestPart("infoList") List<RsltIdxParam> infoList
					, @RequestPart(value = "rsltIdxFileList", required = false) List<MultipartFile> rsltIdxFileList
					, @RequestPart(value = "attachFileList", required = false) List<MultipartFile> attachFileList) {

		String rsltId = rsltService.add(applyId, infoList, rsltIdxFileList, attachFileList);
		RsltIdDto dto = new RsltIdDto();
		dto.setRsltId(rsltId);
		return dto;
	}


	/**
	 * 성과 상세조회
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}")
	public RsltDto get(@PathVariable("rsltId") String rsltId){
		return rsltService.get(rsltId);
	}

	/**
	 * 보완요청
	 * @param rsltId
	 * @param makeupInfo
	 * @param fileList
	 */
	@PutMapping("/{rsltId}/makeup")
	public void modifyMakeup(@PathVariable("rsltId") String rsltId
							, @RequestPart("makeupInfo") RsltMakeupParam makeupInfo
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList){
		rsltService.modifyMakeup(rsltId, makeupInfo, fileList);
	}


	/**
	 * 접수완료
	 * @param rsltId
	 */
	@PutMapping("/{rsltId}/receipt")
	public RsltDto modifyReceipt(HttpServletRequest request, @PathVariable("rsltId") String rsltId){
		rsltService.modifyRsltSttus(rsltId, Code.rsltSttus.접수완료);
		return rsltService.get(rsltId);
	}


	/**
	 * 접수완료취소
	 * @param rsltId
	 * @return
	 */
	@PutMapping("/{rsltId}/receipt-cancel")
	public RsltDto modifyReceiptCancel(HttpServletRequest request, @PathVariable("rsltId") String rsltId){
		rsltService.modifyRsltSttus(rsltId, Code.rsltSttus.제출);
		return rsltService.get(rsltId);
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param rsltId
	 * @param response
	 */
	@GetMapping("/{rsltId}/atchmnfl")
	public void getAtchmnfl(@PathVariable("rsltId") String rsltId, HttpServletResponse response) {
		rsltService.getAtchmnflAllDwld(response, rsltId);
	}

	/**
	 * 첨부파일 다운로드
	 * @param rsltId
	 * @param response
	 * @param attachmentId
	 */
	@GetMapping("/{rsltId}/atchmnfl/{attachmentId}")
	public void getAtchmnflDwld(HttpServletResponse response, @PathVariable("rsltId") String rsltId, @PathVariable("attachmentId") String attachmentId) {
		rsltService.getAtchmnflDwld(response, rsltId, attachmentId);
	}

	/**
	 * 처리이력 조회
	 * @param rsltId
	 * @return
	 */
	@GetMapping("/{rsltId}/hist")
	public JsonList<UsptRsltHist>  getHistList(@PathVariable("rsltId") String rsltId){
		return new JsonList<>(rsltService.getHistList(rsltId));
	}



	/**
	 * 성과현황 조회
	 * @param param
	 * @return
	 */
	@GetMapping("/stats")
	public RsltStatsDto getStats(RsltStatsParam param){
		return rsltService.getStats(param);
	}


	/**
	 * 성과현황 엑셀 다운로드
	 * @param param
	 * @return
	 */
	@GetMapping("/stats/excel-dwld")
	public ModelAndView getStatsExcelDwld(RsltStatsParam param){
		RsltStatsDto dto = rsltService.getStats(param);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("성과현황");
		ExcelSheet<String> sheet = new ExcelSheet<>();
		sheet.setSheetName("성과현황");
		sheet.setTitle("성과현황 목록");

		if(dto != null && dto.getList() != null) {
			List<String> headerList = dto.getListIemList();
			List<List<String>> list = dto.getList();
			String[] arrProperties = new String[headerList.size()];

			for(int idx=0; idx<headerList.size(); idx++) {
				if(idx == 0) {
					arrProperties[idx]= "rn";
				} else if(idx == 1) {
					arrProperties[idx]= "memberNm";
				} else {
					arrProperties[idx]= "val" + idx;
				}
			}
			sheet.setProperties(arrProperties);
			sheet.setHeaders(headerList.toArray());

			List rowList = new ArrayList();
			for(List<String> dataList : list) {
				Map mapData = new HashMap();
				for(int idx=0; idx<dataList.size(); idx++) {
					String data = dataList.get(idx);
					if(idx == 0) {
						mapData.put("rn", data);
					} else if(idx == 1) {
						mapData.put("memberNm", data);
					} else {
						mapData.put("val" + idx, data);
					}
				}
				rowList.add(mapData);
			}
			sheet.addRows(rowList);
		} else {
			throw new InvalidationException("성과현황 정보가 없습니다.");
		}

		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

}
