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

import aicluster.pms.api.pblanc.dto.BsnsPblancDto;
import aicluster.pms.api.pblanc.dto.BsnsPblancListParam;
import aicluster.pms.api.pblanc.dto.BsnsPblancParam;
import aicluster.pms.api.pblanc.dto.PblancIdDto;
import aicluster.pms.api.pblanc.service.BsnsPblancService;
import aicluster.pms.common.dto.BsnsPblancListDto;
import aicluster.pms.common.dto.FrontBsnsPblancDto;
import aicluster.pms.common.entity.UsptBsnsPblanc;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 사업공고관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/bsns-pblanc")
public class BsnsPblancController {

	@Autowired
	private BsnsPblancService bsnsPblancService;

	/**
	 * 목록 조회
	 * @param bsnsPblancListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<BsnsPblancListDto> getList(BsnsPblancListParam bsnsPblancListParam, @RequestParam(defaultValue = "1") Long page) {
		return bsnsPblancService.getList(bsnsPblancListParam, page);
	}

	/**
	 * 목록 엑셀 저장
	 * @param bsnsPblancListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(BsnsPblancListParam bsnsPblancListParam){
		List<BsnsPblancListDto> list = bsnsPblancService.getListExcelDwld(bsnsPblancListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("지원사업공고");
		ExcelSheet<BsnsPblancListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("접수상태", "사업연도", "사업명", "공고명" , "모집유형", "접수시작일", "접수종료일", "공고일", "포털게시");
		sheet.setProperties("pblancSttus", "bsnsYear", "bsnsNm", "pblancNm", "ordtmRcrit", "rceptBgnde", "rceptEndde", "pblancDay", "ntce");
		sheet.setTitle("지원사업공고");
		sheet.setSheetName("지원사업공고");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 등록
	 * @param bsnsPblancParam
	 * @param fileList
	 * @param thumbnailFile
	 * @return
	 */
	@PostMapping("")
	public PblancIdDto add(@RequestPart(value = "info") BsnsPblancParam bsnsPblancParam
					, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList
					, @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
		String pblancId = bsnsPblancService.add(bsnsPblancParam, fileList, thumbnailFile);
		PblancIdDto dto = new PblancIdDto();
		dto.setPblancId(pblancId);
		return dto;
	}

	/**
	 * 상세조회
	 * @param pblancId
	 * @return
	 */
	@GetMapping("/{pblancId}")
	public BsnsPblancDto get(@PathVariable("pblancId") String pblancId) {
		return bsnsPblancService.get(pblancId);
	}

	/**
	 * 수정
	 * @param pblancId
	 * @param bsnsPblancParam
	 * @param fileList
	 * @return
	 */
	@PutMapping("/{pblancId}")
	public BsnsPblancDto modify(@PathVariable("pblancId") String pblancId
					, @RequestPart(value = "info") BsnsPblancParam bsnsPblancParam
					, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList
					, @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
		bsnsPblancService.modify(pblancId, bsnsPblancParam, fileList, thumbnailFile);
		return bsnsPblancService.get(pblancId);
	}

	/**
	 * 썸네일 이미지 파일 다운로드
	 * @param response
	 * @param pblancId
	 */
	@GetMapping("/{pblancId}/thumbnail")
	public void downloadThumbnail(HttpServletResponse response, @PathVariable("pblancId") String pblancId) {
		bsnsPblancService.downloadThumbnail(response, pblancId);
	}

	/**
	 * 첨부파일 전체 다운로드
	 * @param response
	 * @param pblancId
	 * @param attachmentGroupId
	 */
	@GetMapping("/{pblancId}/atchmnfl")
	public void download(HttpServletResponse response, @PathVariable("pblancId") String pblancId) {
		bsnsPblancService.downloadAttFile(response, pblancId);
	}

	/**
	 * 공고 삭제
	 * @param pblancId
	 */
	@DeleteMapping("/{pblancId}")
	public void remove(@PathVariable("pblancId") String pblancId) {
		bsnsPblancService.remove(pblancId);
	}

	/**
	 * 게시여부 설정
	 * @param pblancId
	 * @param ntce
	 */
	@PutMapping("/{pblancId}/notice")
	public void modifyNotice(@PathVariable("pblancId") String pblancId, @RequestBody UsptBsnsPblanc usptBsnsPblanc) {
		bsnsPblancService.modifyNotice(pblancId, usptBsnsPblanc.getNtce());
	}

	/**
	 * 접수상태 설정
	 * @param pblancId
	 * @param pblancSttusCd
	 */
	@PutMapping("/{pblancId}/receipt")
	public void modifyReceipt(@PathVariable("pblancId") String pblancId, @RequestBody UsptBsnsPblanc usptBsnsPblanc) {
		bsnsPblancService.modifyReceipt(pblancId, usptBsnsPblanc.getPblancSttusCd());
	}

	/**
	 * 조기마감
	 * @param pblancId
	 */
	@PutMapping("/{pblancId}/close")
	public void modifyClose(@PathVariable("pblancId") String pblancId) {
		bsnsPblancService.modifyClose(pblancId);
	}


	/**
	 * 상시접수처리
	 * @param pblancId
	 */
	@PutMapping("/{pblancId}/ordinary-receipt")
	public BsnsPblancDto modifyOrdinaryReceipt(@PathVariable("pblancId") String pblancId) {
		bsnsPblancService.modifyOrdinaryReceipt(pblancId);
		return bsnsPblancService.get(pblancId);
	}

	/**
	 * 공고 미리보기
	 * @param pblancId
	 * @return
	 */
	@GetMapping("/{pblancId}/preview")
	public FrontBsnsPblancDto getPreview(@PathVariable("pblancId") String pblancId) {
		return bsnsPblancService.getPreview(pblancId);
	}
}
