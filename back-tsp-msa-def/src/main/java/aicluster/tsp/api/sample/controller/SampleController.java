package aicluster.tsp.api.sample.controller;

import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.sample.dto.SampleParam;
import aicluster.tsp.api.sample.service.SampleService;
import aicluster.tsp.common.sample.entity.CmmtCode;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.view.ExcelView;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
@Api(tags = "샘플 API")
// 스웨거 URL : http://localhost:8083/tsp/swagger-ui.html
public class SampleController {

//	@Autowired
//	private SampleService service;
//	@Autowired
//	private EnvConfig config;
//	@Autowired
//	private AttachmentService attachmentService;
//
//	@ApiOperation(value = "코드 조회", notes = "코드 리스트를 조회합니다.")
//	@ApiResponses({
//			@ApiResponse(code = 200, message = "OK !!!!!<br/>엔터 예시<br/>", response = CmmtCode.class)
//	})
//	@GetMapping("/codes")
//	public List<CmmtCode> getCodes() {
//		return service.getCodeList();
//	}
//
//
//	@ApiOperation(value = "getApiImplicitParam 테스트", notes = "swagger 에서 사용되는 @ApiImplicitParam을 사용한 샘플 코드입니다.")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "id", value = "id - value", required = true, dataType = "String", defaultValue = "기본값"),
//			@ApiImplicitParam(name = "page", value = "페이지", required = true, dataType = "Long"),
//			@ApiImplicitParam(name = "itemsPerPage", value = "한 페이지에 보여질 개수", dataType = "Long")
//	})
//	@GetMapping("/getApiImplicitParam")
//	public void getApiImplicitParam(String id, Long page, Long itemsPerPage) {
//		service.getParam(id, page, itemsPerPage);
//	}
//
//
//	@ApiOperation(value = "getApiParam 테스트", notes = "swagger 에서 사용되는 @ApiParam을 사용한 샘플 코드입니다")
//	@GetMapping("/getApiParam")
//	public void getApiParam(
//			@ApiParam(value = "id - value", required = true) @RequestParam String id,
//			@ApiParam(value = "페이지", required = true) @RequestParam Long page,
//			@ApiParam(value = "한 페이지에 보여질 개수", required = true) @RequestParam Long itemsPerPage) {
//		service.getParam(id, page, itemsPerPage);
//	}
//
//
//	/*
//	{
//		"cmmtCodeThree":
//		{
//			"codeGroup": "11111",
//			"code": "22222"
//		},
//		"cmmtCodeTwo":
//		{
//			"codeNm": "33333",
//			"remark": "44444"
//		}
//	}
//	* */
//
//	@ApiOperation(value = "DTO param 테스트", notes = "DTO 로 받을시 샘플 코드 입니다.")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "param", value = "param", dataType = "SampleParam")
//	})
//	@GetMapping("/paramTest")
//	public void paramTest(@RequestBody(required = false) SampleParam param) {
//		service.paramTest(param);
//	}
//
//	/*
//	 * =====================================================================================
//	 * 엑셀 다운로드
//	 * =====================================================================================
//	 * */
//	@ApiOperation(value = "엑셀 다운로드", notes = "엑셀 다운로드 샘플 코드 입니다.")
//	@GetMapping("/excel-dwld")
//	public ModelAndView getListExcelDwld() {
//		List<CmmtCode> list = service.getListExcelDwld();
//		ExcelWorkbook wb = new ExcelWorkbook();
//		wb.setFilename("파일명");
//		ExcelSheet<CmmtCode> sheet = new ExcelSheet<>();
//		ExcelMergeRows mergeRegions = new ExcelMergeRows();
//		sheet.setMergeRegions(mergeRegions);
//		sheet.addRows(list);
//		sheet.setHeaders("코드그룹", "코드", "코드명", "리마크", "코드타입", "사용여부", "정렬", "등록자", "등록일", "수정자", "수정일");
//		sheet.setProperties("codeGroup", "code", "codeNm", "remark", "codeType", "enabled", "sortOrder", "creatorId", "createdDt", "updaterId");
//		sheet.setTitle("타이틀명");
//		sheet.setSheetName("시트명");
//		wb.addSheet(sheet);
//		return ExcelView.getView(wb);
//	}
//
//	/*
//	 * =====================================================================================
//	 * 파일 업로드
//	 * =====================================================================================
//	 * */
//	@ApiOperation(value = "파일 한개 업로드 - No Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>cmmt_attachment 테이블에 single로 데이터를 저장")
//	@PostMapping("/upload-no-group")
//	public void uploadFile_noGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment) {
//		service.uploadFile_noGroup(attachment);
//	}
//
//	@ApiOperation(value = "파일 한개 업로드 - Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>attachment_group_id로 cmmt_attachment 테이블에 데이터를 저장")
//	@PostMapping("/upload-group/{attachmentGroupId}")
//	public void uploadFile_toGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment, @PathVariable String attachmentGroupId) {
//		service.uploadFile_toGroup(attachment, attachmentGroupId);
//	}
//
//	@ApiOperation(value = "파일 한개 업로드 - New Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>attachment_group을 새로 만들고 cmmt_attachment 테이블에 데이터를 저장")
//	@PostMapping("/upload-new-group")
//	public void uploadFile_toNewGroup(@RequestPart(value = "attachment", required = false) MultipartFile attachment) {
//		service.uploadFile_toNewGroup(attachment);
//	}
//
//	@ApiOperation(value = "파일 여러개 업로드 - Group", notes = "파일 여러 업로드 샘플 코드 입니다.<br/>attachment_group_id로 cmmt_attachment 테이블에 데이터를 저장")
//	@PostMapping("/uploads-group/{attachmentGroupId}")
//	public void uploadFiles_toGroup(@RequestPart(value = "attachment", required = false) List<MultipartFile> attachments, @PathVariable String attachmentGroupId) {
//		service.uploadFiles_toGroup(attachments, attachmentGroupId);
//	}
//
//	@ApiOperation(value = "파일 여러개 업로드 - New Group", notes = "파일 여러개 업로드 샘플 코드 입니다.<br/>attachment_group을 새로 만들고 cmmt_attachment 테이블에 데이터를 저장")
//	@PostMapping("/uploads-new-group")
//	public void uploadFiles_toNewGroup(@RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {
//		service.uploadFiles_toNewGroup(attachments);
//	}
//
//	/*
//	 * =====================================================================================
//	 * 파일 다운로드
//	 * =====================================================================================
//	 * */
//	@ApiOperation(value = "파일 한개 다운로드", notes = "attachmentId 로 파일을 다운로드 한다.")
//	@GetMapping("/file-dwld/{attachmentId}")
//	public void fileDownload(HttpServletResponse response, @PathVariable String attachmentId) {
//		service.fileDownload(response, attachmentId);
//	}
//
//	@ApiOperation(value = "파일 여러개개 다운로드", notes = "String[] attachmentId 로 파일을 여러개 다운로드 한다.")
//	@GetMapping("/file-dwlds")
//	public void fileDownloads(HttpServletResponse response, String[] attachmentIds) {
//		service.fileDownloads(response, attachmentIds);
//	}
//
//	@ApiOperation(value = "파일 한개 다운로드 - contentType", notes = "attachmentId 로 파일을 contentType 에 맞게 해서 다운로드 한다.")
//	@GetMapping("/file-dwld-contentType/{attachmentId}")
//	public void downloadFile_contentType(HttpServletResponse response, @PathVariable String attachmentId) {
//		service.downloadFile_contentType(response, attachmentId);
//	}
//
//	@ApiOperation(value = "파일 그룹 다운로드", notes = "attachmentGroupId 로 해서 해당 파일들을 전부 다운로드 한다.")
//	@GetMapping("/file-dwld-groupfiles/{attachmentGroupId}")
//	public void fileDownload_groupfiles(HttpServletResponse response, @PathVariable String attachmentGroupId) {
//		service.fileDownload_groupfiles(response, attachmentGroupId);
//	}
//
//
//	/*
//	 * =====================================================================================
//	 * 파일 삭제
//	 * =====================================================================================
//	 * */
//
//	@ApiOperation(value = "파일 삭제", notes = "attachmentId로 파일을 삭제한다.")
//	@GetMapping("/file-remove/{attachmentId}")
//	public void removeFile(@PathVariable String attachmentId) {
//		service.removeFile(attachmentId);
//	}
//
//	@ApiOperation(value = "파일 삭제 - Group", notes = "attachmentGroupId로 파일 및 그룹을 삭제한다.")
//	@GetMapping("/file-remove-group/{attachmentGroupId}")
//	public void removeFiles_group(@PathVariable String attachmentGroupId) {
//		service.removeFiles_group(attachmentGroupId);
//	}
//
//	@ApiOperation(value = "파일 삭제 - Physical", notes = "attachmentId로 파일은 삭제 하지만 데이터베이스에 데이터는 지우지 않는다.")
//	@GetMapping("/file-remove-physical/{attachmentId}")
//	public void removePhysicalFileOnly(@PathVariable String attachmentId) {
//		service.removePhysicalFileOnly(attachmentId);
//	}
//
//	@ApiOperation(value = "파일 삭제 - Physical Group", notes = "attachmentGroupId로 파일은 삭제 하지만 데이터베이스에 데이터는 지우지 않는다.")
//	@GetMapping("/file-remove-physical-group/{attachmentGroupId}")
//	public void removePhysicalFilesOnly_group(@PathVariable String attachmentGroupId) {
//		service.removePhysicalFilesOnly_group(attachmentGroupId);
//	}
	/*
	 * =====================================================================================
	 * 실제 데이터 추가시 참조 샘플
	 * =====================================================================================
	 * */
//	@ApiOperation(value = "파일 한개 업로드 - No Group", notes = "파일 한개 업로드 샘플 코드 입니다.<br/>cmmt_attachment 테이블에 single로 데이터를 저장")
//	@PostMapping("/insert-sample/{sampleid}")
//	public void uploadFile_noGroup_sampleId(@PathVariable String sampleid,
//											@RequestPart(value = "sampleparam", required = false) SampleParam param,
//											@RequestPart(value = "image1", required = false) MultipartFile image1,
//											@RequestPart(value = "image2", required = false) MultipartFile image2,
//											@RequestPart(value = "image3", required = false) List<MultipartFile> image3) {
//		service.uploadFile_noGroup_sampleId(sampleid, param, image1, image2, image3);
//	}
}
