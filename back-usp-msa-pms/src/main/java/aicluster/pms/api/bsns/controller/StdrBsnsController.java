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
import aicluster.pms.api.bsns.dto.IoeSetupParam;
import aicluster.pms.api.bsns.dto.RsltIdx;
import aicluster.pms.api.bsns.dto.StdrBsnsBasicDto;
import aicluster.pms.api.bsns.dto.StdrBsnsBasicParam;
import aicluster.pms.api.bsns.dto.StdrBsnsCdDto;
import aicluster.pms.api.bsns.dto.StdrBsnsListParam;
import aicluster.pms.api.bsns.dto.StdrBsnsTaskDto;
import aicluster.pms.api.bsns.service.StdrBsnsService;
import aicluster.pms.common.dto.ApplyLimitDto;
import aicluster.pms.common.dto.BhExmntDto;
import aicluster.pms.common.dto.IoeSetupDto;
import aicluster.pms.common.dto.JobStepDto;
import aicluster.pms.common.dto.StdrBsnsListDto;
import aicluster.pms.common.entity.UsptStdrAtchmnflSetup;
import aicluster.pms.common.entity.UsptStdrBhExmnt;
import aicluster.pms.common.entity.UsptStdrJobStep;
import aicluster.pms.common.entity.UsptStdrRsltIdx;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;

/**
 * 기준사업 관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/stdr-bsns")
public class StdrBsnsController {

	@Autowired
	private StdrBsnsService stdrBsnsService;

	/**
	 * 기준사업관리 목록 조회
	 * @param stdrBsnsListParam
	 * @param page
	 * @return
	 */
	@GetMapping("")
	public CorePagination<StdrBsnsListDto> getList(StdrBsnsListParam stdrBsnsListParam, @RequestParam(defaultValue = "1") Long page){
		return stdrBsnsService.getList(stdrBsnsListParam, page);
	}
	/**
	 * 기준사업관리 목록 엑셀 저장
	 * @param stdrBsnsListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(StdrBsnsListParam stdrBsnsListParam){

		List<StdrBsnsListDto> list = stdrBsnsService.getListExcelDwld(stdrBsnsListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("기준사업목록");
		ExcelSheet<StdrBsnsListDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("기준사업코드", "기준사업명", "시작년도", "분류" , "담당부서", "담당자명", "등록일");
		sheet.setProperties("stdrBsnsCd", "stdrBsnsNm", "beginYear", "bsnsClNm", "chrgDeptNm", "chargerNm", "regDt");
		sheet.setTitle("기준사업 목록");
		sheet.setSheetName("기준사업");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 사업 등록을 위한 기준사업 목록 조회
	 * @param stdrBsnsListParam
	 * @param page
	 * @return
	 */
	@GetMapping("/bsns")
	public CorePagination<StdrBsnsListDto> getListForBsns(StdrBsnsListParam stdrBsnsListParam, @RequestParam(defaultValue = "1") Long page){
		return stdrBsnsService.getListForBsns(stdrBsnsListParam, page);
	}


	/**
	 * 기준사업 저장
	 * @param stdrBsns
	 * @return
	 */
	@PostMapping("")
	public StdrBsnsCdDto add(@RequestBody StdrBsnsBasicParam stdrBsns) {
		String stdrBsnsCd = stdrBsnsService.add(stdrBsns);
		StdrBsnsCdDto dto = new StdrBsnsCdDto();
		dto.setStdrBsnsCd(stdrBsnsCd);
		return dto;
	}


	/**
	 * 기본정보 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}")
	public StdrBsnsBasicDto getBasicInfo(@PathVariable("stdrBsnsCd") String stdrBsnsCd) {
		return stdrBsnsService.getBasicInfo(stdrBsnsCd);
	}
	/**
	 * 기본정보 수정
	 * @param stdrBsnsCd
	 * @param stdrBsnsBasicParam
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}")
	public StdrBsnsBasicDto modifyBasicInfo(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody StdrBsnsBasicParam stdrBsnsBasicParam) {
		stdrBsnsService.modifyBasicInfo(stdrBsnsCd, stdrBsnsBasicParam);
		return stdrBsnsService.getBasicInfo(stdrBsnsCd);
	}


	/**
	 * 과제정보 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/task")
	public StdrBsnsTaskDto getTaskInfo(@PathVariable("stdrBsnsCd") String stdrBsnsCd) {
		return stdrBsnsService.getTaskInfo(stdrBsnsCd);
	}
	/**
	 * 과제정보 수정
	 * @param stdrBsnsCd
	 * @param taskInfo
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/task")
	public StdrBsnsTaskDto modifyTaskInfo(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody StdrBsnsTaskDto taskInfo) {
		stdrBsnsService.modifyTaskInfo(stdrBsnsCd, taskInfo);
		return stdrBsnsService.getTaskInfo(stdrBsnsCd);
	}


	/**
	 * 업무단계 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/job-step")
	public JsonList<JobStepDto> getJobStepList(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getJobStepList(stdrBsnsCd);
	}
	/**
	 * 업무단계 수정
	 * @param stdrBsnsCd
	 * @param jobStepList
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/job-step")
	public JsonList<JobStepDto> modifyJobStep(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<UsptStdrJobStep> jobStepList){
		stdrBsnsService.modifyJobStep(stdrBsnsCd, jobStepList);
		return stdrBsnsService.getJobStepList(stdrBsnsCd);
	}


	/**
	 * 신청제한 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/apply-limit")
	public ApplyLimitDto getApplyLimit(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getApplyLimit(stdrBsnsCd);
	}
	/**
	 * 신청제한 수정
	 * @param stdrBsnsCd
	 * @param applyLimitDto
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/apply-limit")
	public ApplyLimitDto modifyApplyLimit(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody ApplyLimitDto applyLimitDto){
		stdrBsnsService.modifyApplyLimit(stdrBsnsCd, applyLimitDto);
		return stdrBsnsService.getApplyLimit(stdrBsnsCd);
	}


	/**
	 * 사전검토 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/bh-exmnt")
	public JsonList<BhExmntDto> getBhExmnt(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getBhExmnt(stdrBsnsCd);
	}
	/**
	 * 사전검토 수정
	 * @param stdrBsnsCd
	 * @param bhExmntList
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/bh-exmnt")
	public JsonList<BhExmntDto> modifyBhExmnt(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<UsptStdrBhExmnt> bhExmntList){
		stdrBsnsService.modifyBhExmnt(stdrBsnsCd, bhExmntList);
		return stdrBsnsService.getBhExmnt(stdrBsnsCd);
	}


	/**
	 * 비목 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/ioe")
	public JsonList<IoeSetupDto> getIoe(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getIoe(stdrBsnsCd);
	}
	/**
	 * 비목 수정
	 * @param stdrBsnsCd
	 * @param ioeSetupParamList
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/ioe")
	public JsonList<IoeSetupDto> modifyIoe(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<IoeSetupParam> ioeSetupParamList){
		stdrBsnsService.modifyIoe(stdrBsnsCd, ioeSetupParamList);
		return stdrBsnsService.getIoe(stdrBsnsCd);
	}


	/**
	 * 성과지표 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/rslt-idx")
	public JsonList<UsptStdrRsltIdx> getRsltIdx(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getRsltIdx(stdrBsnsCd);
	}
	/**
	 * 성과지표 수정
	 * @param stdrBsnsCd
	 * @param rsltIdxList
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/rslt-idx")
	public JsonList<UsptStdrRsltIdx> modifyRsltIdx(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<UsptStdrRsltIdx> rsltIdxList){
		stdrBsnsService.modifyRsltIdx(stdrBsnsCd, rsltIdxList);
		return stdrBsnsService.getRsltIdx(stdrBsnsCd);
	}
	/**
	 * 성과지표 삭제
	 * @param stdrBsnsCd
	 * @param rsltIdxIdList
	 * @return
	 */
	@DeleteMapping("/{stdrBsnsCd}/rslt-idx")
	public JsonList<UsptStdrRsltIdx> removeRsltIdx(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<RsltIdx> rsltIdxIdList){
		stdrBsnsService.removeRsltIdx(stdrBsnsCd, rsltIdxIdList);
		return stdrBsnsService.getRsltIdx(stdrBsnsCd);
	}


	/**
	 * 첨부파일설정 조회
	 * @param stdrBsnsCd
	 * @return
	 */
	@GetMapping("/{stdrBsnsCd}/atchmnfl")
	public JsonList<UsptStdrAtchmnflSetup> getAtchmnfl(@PathVariable("stdrBsnsCd") String stdrBsnsCd){
		return stdrBsnsService.getAtchmnfl(stdrBsnsCd);
	}
	/**
	 * 첨부파일 설정 수정
	 * @param stdrBsnsCd
	 * @param setupList
	 * @param fileList
	 * @return
	 */
	@PutMapping("/{stdrBsnsCd}/atchmnfl")
	public JsonList<UsptStdrAtchmnflSetup> modifyAtchmnfl(@PathVariable("stdrBsnsCd") String stdrBsnsCd
													, @RequestPart(value = "setupList") List<UsptStdrAtchmnflSetup> setupList
													, @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList){
		stdrBsnsService.modifyAtchmnfl(stdrBsnsCd, setupList, fileList);
		return stdrBsnsService.getAtchmnfl(stdrBsnsCd);
	}
	/**
	 * 첨부파일 삭제
	 * @param stdrBsnsCd
	 * @param atchmnflSetupIdList
	 * @return
	 */
	@DeleteMapping("/{stdrBsnsCd}/atchmnfl")
	public JsonList<UsptStdrAtchmnflSetup> removeAtchmnfl(@PathVariable("stdrBsnsCd") String stdrBsnsCd, @RequestBody List<AtchmnflSetup> atchmnflSetupIdList){
		stdrBsnsService.removeAtchmnfl(stdrBsnsCd, atchmnflSetupIdList);
		return stdrBsnsService.getAtchmnfl(stdrBsnsCd);
	}

	/**
	 * 기준사업 삭제
	 * @param stdrBsnsCd
	 */
	@DeleteMapping("/{stdrBsnsCd}")
	public void remove(@PathVariable("stdrBsnsCd") String stdrBsnsCd) {
		stdrBsnsService.remove(stdrBsnsCd);
	}
}
