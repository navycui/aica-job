package aicluster.pms.api.bsns.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.bsns.dto.EntrpsSttusListParam;
import aicluster.pms.api.bsns.dto.EntrpsSttusSendParam;
import aicluster.pms.api.bsns.service.EntrpsSttusSerivce;
import aicluster.pms.api.evl.dto.DspthResultDto;
import aicluster.pms.common.dto.EntrpsSttusListDto;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 업체현황 조회
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/entrps-sttus")
public class EntrpsSttusController {

	@Autowired
	private EntrpsSttusSerivce entrpsSttusSerivce;

	/**
	 * 업체현황 목록 조회
	 * @param entrpsSttusListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<EntrpsSttusListDto> getList(EntrpsSttusListParam entrpsSttusListParam, @RequestParam(defaultValue = "1") Long page){
		return entrpsSttusSerivce.getList(entrpsSttusListParam, page);
	}

	/**
	 * 업체현황 목록 엑셀 저장
	 * @param request
	 * @param entrpsSttusListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(HttpServletRequest request, EntrpsSttusListParam entrpsSttusListParam){
		List<EntrpsSttusListDto> list = entrpsSttusSerivce.getListExcelDwld(request, entrpsSttusListParam);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("업체현황");
		ExcelSheet<EntrpsSttusListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("사업연도", "사업명", "공고명", "과제유형" , "과제명", "업체명", "사업자등록번호", "참여구분", "담당자", "휴대폰번호", "이메일", "최근발송일");
		sheet.setProperties("bsnsYear", "bsnsNm", "pblancNm", "taskType", "taskNm", "entrpsNm", "bizrno", "partcptnDiv", "rspnberNm", "mbtlnum", "email", "recentSendDate");
		sheet.setTitle("업체현황");
		sheet.setSheetName("업체현황");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 메시지 전송
	 * @param param
	 * @return
	 */
	@PostMapping("/send-msg")
	public DspthResultDto sendMsg(@RequestBody EntrpsSttusSendParam param) {
		return entrpsSttusSerivce.sendMsg(param);
	}

}
