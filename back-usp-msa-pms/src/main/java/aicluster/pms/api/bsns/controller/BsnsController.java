package aicluster.pms.api.bsns.controller;

import java.util.List;

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

import aicluster.pms.api.bsns.dto.AtchmnflSetup;
import aicluster.pms.api.bsns.dto.BsnsBasicParam;
import aicluster.pms.api.bsns.dto.BsnsCdDto;
import aicluster.pms.api.bsns.dto.BsnsListParam;
import aicluster.pms.api.bsns.dto.BsnsTaskDto;
import aicluster.pms.api.bsns.dto.IoeSetupParam;
import aicluster.pms.api.bsns.dto.RsltIdx;
import aicluster.pms.api.bsns.service.BsnsService;
import aicluster.pms.common.dto.ApplyLimitDto;
import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.dto.BsnsBasicDto;
import aicluster.pms.common.dto.BsnsListDto;
import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsAtchmnflSetup;
import aicluster.pms.common.entity.UsptBsnsBhExmnt;
import aicluster.pms.common.entity.UsptBsnsJobStep;
import aicluster.pms.common.entity.UsptBsnsRsltIdx;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 사업관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/bsns")
public class BsnsController {

	@Autowired
	private BsnsService bsnsService;

	/**
	 * 사업관리 목록 조회
	 * @param bsnsListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<BsnsListDto> getList(BsnsListParam bsnsListParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsService.getList(bsnsListParam, page);
	}
	/**
	 * 사업관리 목록 엑셀 저장
	 * @param bsnsListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(BsnsListParam bsnsListParam){

		List<BsnsListDto> list = bsnsService.getListExcelDwld(bsnsListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("사업목록");
		ExcelSheet<BsnsListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("사업코드", "사업명", "사업연도", "기준사업코드" , "기준사업명", "담당부서", "담당자명", "등록일");
		sheet.setProperties("bsnsCd", "bsnsNm", "bsnsYear", "stdrBsnsCd", "stdrBsnsNm", "chrgDeptNm", "chargerNm", "regDt");
		sheet.setTitle("사업 목록");
		sheet.setSheetName("사업");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 지원사업공고 등록을 위한 사업 목록 조회
	 * @param bsnsListParam
	 * @param page
	 * @return
	 */
	@GetMapping("/pblanc")
	public CorePagination<BsnsListDto> getListForPblanc(BsnsListParam bsnsListParam, @RequestParam(defaultValue = "1") Long page){
		return bsnsService.getListForPblanc(bsnsListParam, page);
	}

	/**
	 * 사업 등록
	 * @param bsnsBasicParam
	 * @return
	 */
	@PostMapping("")
	public BsnsCdDto add(@RequestBody BsnsBasicParam bsnsBasicParam) {
		String bsnsCd = bsnsService.add(bsnsBasicParam);
		BsnsCdDto dto = new BsnsCdDto();
		dto.setBsnsCd(bsnsCd);
		return dto;
	}


	/**
	 * 기본정보 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}")
	public BsnsBasicDto getBasicInfo(@PathVariable("bsnsCd") String bsnsCd) {
		return bsnsService.getBasicInfo(bsnsCd);
	}
	/**
	 * 기본정보 수정
	 * @param bsnsCd
	 * @param bsnsBasicParam
	 * @return
	 */
	@PutMapping("/{bsnsCd}")
	public BsnsBasicDto modifyBasicInfo(@PathVariable("bsnsCd") String bsnsCd, @RequestBody BsnsBasicParam bsnsBasicParam) {
		bsnsBasicParam.setBsnsCd(bsnsCd);
		bsnsService.modifyBasicInfo(bsnsBasicParam);
		return bsnsService.getBasicInfo(bsnsCd);
	}


	/**
	 * 과제정보 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/task")
	public BsnsTaskDto getTaskInfo(@PathVariable("bsnsCd") String bsnsCd) {
		return bsnsService.getTaskInfo(bsnsCd);
	}
	/**
	 * 과제정보 수정
	 * @param bsnsCd
	 * @param bsnsTaskInfo
	 * @return
	 */
	@PutMapping("/{bsnsCd}/task")
	public BsnsTaskDto modifyTaskInfo(@PathVariable("bsnsCd") String bsnsCd, @RequestBody BsnsTaskDto bsnsTaskInfo) {
		bsnsTaskInfo.setBsnsCd(bsnsCd);
		bsnsService.modifyTaskInfo(bsnsTaskInfo);
		return bsnsService.getTaskInfo(bsnsCd);
	}


	/**
	 * 업무단계 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/job-step")
	public JsonList<JobStepDto> getJobStepList(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getJobStepList(bsnsCd);
	}
	/**
	 * 업무단계 수정
	 * @param bsnsCd
	 * @param jobStepList
	 * @return
	 */
	@PutMapping("/{bsnsCd}/job-step")
	public JsonList<JobStepDto> modifyJobStep(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<UsptBsnsJobStep> jobStepList){
		bsnsService.modifyJobStep(bsnsCd, jobStepList);
		return bsnsService.getJobStepList(bsnsCd);
	}


	/**
	 * 신청제한 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/apply-limit")
	public ApplyLimitDto getApplyLimit(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getApplyLimit(bsnsCd);
	}
	/**
	 * 신청제한 수정
	 * @param bsnsCd
	 * @param applyLimitDto
	 * @return
	 */
	@PutMapping("/{bsnsCd}/apply-limit")
	public ApplyLimitDto modifyApplyLimit(@PathVariable("bsnsCd") String bsnsCd, @RequestBody ApplyLimitDto applyLimitDto){
		bsnsService.modifyApplyLimit(bsnsCd, applyLimitDto);
		return bsnsService.getApplyLimit(bsnsCd);
	}


	/**
	 * 사전검토 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/bh-exmnt")
	public JsonList<BhExmntDto> getBhExmnt(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getBhExmnt(bsnsCd);
	}
	/**
	 * 사전검토 수정
	 * @param bsnsCd
	 * @param bhExmntList
	 * @return
	 */
	@PutMapping("/{bsnsCd}/bh-exmnt")
	public JsonList<BhExmntDto> modifyBhExmnt(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<UsptBsnsBhExmnt> bhExmntList){
		bsnsService.modifyBhExmnt(bsnsCd, bhExmntList);
		return bsnsService.getBhExmnt(bsnsCd);
	}


	/**
	 * 비목 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/ioe")
	public JsonList<IoeSetupDto> getIoe(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getIoe(bsnsCd);
	}
	/**
	 * 비목 수정
	 * @param bsnsCd
	 * @param ioeSetupParamList
	 * @return
	 */
	@PutMapping("/{bsnsCd}/ioe")
	public JsonList<IoeSetupDto> modifyIoe(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<IoeSetupParam> ioeSetupParamList){
		bsnsService.modifyIoe(bsnsCd, ioeSetupParamList);
		return bsnsService.getIoe(bsnsCd);
	}


	/**
	 * 성과지표 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/rslt-idx")
	public JsonList<UsptBsnsRsltIdx> getRsltIdx(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getRsltIdx(bsnsCd);
	}
	/**
	 * 성과지표 수정
	 * @param bsnsCd
	 * @param rsltIdxList
	 * @return
	 */
	@PutMapping("/{bsnsCd}/rslt-idx")
	public JsonList<UsptBsnsRsltIdx> modifyRsltIdx(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<UsptBsnsRsltIdx> rsltIdxList){
		bsnsService.modifyRsltIdx(bsnsCd, rsltIdxList);
		return bsnsService.getRsltIdx(bsnsCd);
	}
	/**
	 * 성과지표 삭제
	 * @param bsnsCd
	 * @param rsltIdxIdList
	 * @return
	 */
	@DeleteMapping("/{bsnsCd}/rslt-idx")
	public JsonList<UsptBsnsRsltIdx> removeRsltIdx(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<RsltIdx> rsltIdxIdList){
		bsnsService.removeRsltIdx(bsnsCd, rsltIdxIdList);
		return bsnsService.getRsltIdx(bsnsCd);
	}


	/**
	 * 첨부파일설정 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/atchmnfl")
	public JsonList<UsptBsnsAtchmnflSetup> getAtchmnfl(@PathVariable("bsnsCd") String bsnsCd){
		return bsnsService.getAtchmnfl(bsnsCd);
	}
	/**
	 * 첨부파일설정 수정
	 * @param bsnsCd
	 * @param setupList
	 * @param fileList
	 * @return
	 */
	@PutMapping("/{bsnsCd}/atchmnfl")
	public JsonList<UsptBsnsAtchmnflSetup> modifyAtchmnfl(@PathVariable("bsnsCd") String bsnsCd
													, @RequestPart(value = "setupList") List<UsptBsnsAtchmnflSetup> setupList
													, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList){
		bsnsService.modifyAtchmnfl(bsnsCd, setupList, fileList);
		return bsnsService.getAtchmnfl(bsnsCd);
	}
	/**
	 * 첨부파일 삭제
	 * @param bsnsCd
	 * @param atchmnflSetupIdList
	 * @return
	 */
	@DeleteMapping("/{bsnsCd}/atchmnfl")
	public JsonList<UsptBsnsAtchmnflSetup> removeAtchmnfl(@PathVariable("bsnsCd") String bsnsCd, @RequestBody List<AtchmnflSetup> atchmnflSetupIdList){
		bsnsService.removeAtchmnfl(bsnsCd, atchmnflSetupIdList);
		return bsnsService.getAtchmnfl(bsnsCd);
	}

	/**
	 * 사업 삭제
	 * @param bsnsCd
	 */
	@DeleteMapping("/{bsnsCd}")
	public void remove(@PathVariable("bsnsCd") String bsnsCd) {
		bsnsService.remove(bsnsCd);
	}


	/**
	 * 과제분야 목록 조회
	 * @param bsnsCd
	 * @return
	 */
	@GetMapping("/{bsnsCd}/apply-realm")
	public JsonList<UsptBsnsApplyRealm> getApplyRealmList(@PathVariable("bsnsCd") String bsnsCd){
		return new JsonList<>(bsnsService.getApplyRealmList(bsnsCd));
	}

}
