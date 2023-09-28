package aicluster.pms.api.pblanc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import aicluster.pms.api.pblanc.dto.PblancEvlTargetParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancIdDto;
import aicluster.pms.api.pblanc.dto.SlctnPblancListParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancParam;
import aicluster.pms.api.pblanc.dto.SlctnPblancTargetListParam;
import aicluster.pms.api.pblanc.service.SlctnPblancService;
import aicluster.pms.common.dto.BsnsPblancListDto;
import aicluster.pms.common.dto.EvlApplcntListDto;
import aicluster.pms.common.dto.EvlPlanPblancStepDto;
import aicluster.pms.common.dto.FrontSlctnPblancDto;
import aicluster.pms.common.dto.SlctnPblancDto;
import aicluster.pms.common.dto.SlctnPblancListDto;
import aicluster.pms.common.entity.UsptSlctnPblanc;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 선정결과공고 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/slctn-pblanc")
public class SlctnPblancController {

	@Autowired
	private SlctnPblancService slctnPblancService;

	/**
	 * 선정공고 목록 조회
	 * @param slctnPblancListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<SlctnPblancListDto> getList(SlctnPblancListParam slctnPblancListParam, @RequestParam(defaultValue = "1") Long page) {
		return slctnPblancService.getList(slctnPblancListParam, page);
	}

	/**
	 * 선정공고 목록 엑셀 다운로드
	 * @param slctnPblancListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(SlctnPblancListParam slctnPblancListParam){
		List<SlctnPblancListDto> list = slctnPblancService.getListExcelDwld(slctnPblancListParam);
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("선정결과공고");
		ExcelSheet<SlctnPblancListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("포털게시", "사업연도", "사업명", "공고명" , "모집유형", "담당부서", "공고일", "등록일");
		sheet.setProperties("ntce", "bsnsYear", "bsnsNm", "slctnPblancNm", "ordtmRcrit", "chrgDeptNm", "slctnPblancDay", "regDt");
		sheet.setTitle("선정결과공고");
		sheet.setSheetName("선정결과공고");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 공고대상 목록 조회
	 * @param slctnPblancTargetListParam
	 * @param page
	 * @return
	 */
	@GetMapping("/bsns-pblanc")
	public CorePagination<BsnsPblancListDto> getPblancList(SlctnPblancTargetListParam slctnPblancTargetListParam, @RequestParam(defaultValue = "1") Long page) {
		return slctnPblancService.getPblancList(slctnPblancTargetListParam, page);
	}

	/**
	 * 공고대상 선정구분 목록조회
	 * @param pblancId
	 * @param rceptOdr
	 * @return
	 */
	@GetMapping("/bsns-pblanc/evl-step")
	public JsonList<EvlPlanPblancStepDto> getPblancEvlStepList(String pblancId, Integer rceptOdr) {
		return slctnPblancService.getPblancEvlStepList(pblancId, rceptOdr);
	}

	/**
	 * 선정공고 대상자 목록 조회
	 * @param pblancEvlTargetParam
	 * @param page
	 * @return
	 */
	@GetMapping("/bsns-pblanc/evl-target")
	public CorePagination<EvlApplcntListDto> getPblancEvlTargetList(PblancEvlTargetParam pblancEvlTargetParam, @RequestParam(defaultValue = "1") Long page) {
		return slctnPblancService.getPblancEvlTargetList(pblancEvlTargetParam, page);
	}

	/**
	 * 선정공고 등록
	 * @param slctnPblancParam
	 * @return
	 */
	@PostMapping("")
	public SlctnPblancIdDto add(@RequestPart(value = "info") SlctnPblancParam slctnPblancParam
					, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		String slctnPblancId = slctnPblancService.add(slctnPblancParam, fileList);
		SlctnPblancIdDto dto = new SlctnPblancIdDto();
		dto.setSlctnPblancId(slctnPblancId);
		return dto;
	}

	/**
	 * 선정공고 상세조회
	 * @param slctnPblancId
	 * @return
	 */
	@GetMapping("/{slctnPblancId}")
	public SlctnPblancDto get(@PathVariable("slctnPblancId") String slctnPblancId) {
		return slctnPblancService.get(slctnPblancId);
	}

	/**
	 * 선정공고 수정
	 * @param slctnPblancId
	 * @param slctnPblancParam
	 * @return
	 */
	@PutMapping("/{slctnPblancId}")
	public SlctnPblancDto modify(@PathVariable("slctnPblancId") String slctnPblancId
							, @RequestPart(value = "info") SlctnPblancParam slctnPblancParam
							, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		slctnPblancService.modify(slctnPblancId, slctnPblancParam, fileList);
		return slctnPblancService.get(slctnPblancId);
	}

	/**
	 * 선정공고 포털 게시여부 설정
	 * @param slctnPblancId
	 * @param usptSlctnPblanc
	 */
	@PutMapping("/{slctnPblancId}/notice")
	public void modifyNotice(@PathVariable("slctnPblancId") String slctnPblancId, @RequestBody UsptSlctnPblanc usptSlctnPblanc) {
		slctnPblancService.modifyNotice(slctnPblancId, usptSlctnPblanc.getNtce());
	}

	/**
	 * 선정공고 삭제
	 * @param slctnPblancId
	 */
	@DeleteMapping("/{slctnPblancId}")
	public void remove(@PathVariable("slctnPblancId") String slctnPblancId) {
		slctnPblancService.remove(slctnPblancId);
	}

	/**
	 * 천제파일 다운로드
	 * @param response
	 * @param slctnPblancId
	 * @param attachmentGroupId
	 */
	@GetMapping("/{slctnPblancId}/atchmnfl")
	public void download(HttpServletResponse response, @PathVariable("slctnPblancId") String slctnPblancId) {
		slctnPblancService.downloadAttFile(response, slctnPblancId);
	}

	/**
	 * 미리보기
	 * @param slctnPblancId
	 * @return
	 */
	@GetMapping("/{slctnPblancId}/preview")
	public FrontSlctnPblancDto getPreview(@PathVariable("slctnPblancId") String slctnPblancId) {
		return slctnPblancService.getPreview(slctnPblancId);

	}
}
