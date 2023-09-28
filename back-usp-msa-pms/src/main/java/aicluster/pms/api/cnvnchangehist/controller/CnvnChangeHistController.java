package aicluster.pms.api.cnvnchangehist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.pms.api.cnvnchange.dto.CnvnChangeParam;
import aicluster.pms.api.cnvnchangehist.dto.CnvnChangeHistBaseInfoDto;
import aicluster.pms.api.cnvnchangehist.dto.CnvnChangeHistParam;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnApplcntChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnSclpstChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptCnvnTaskInfoChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskPartcptsChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskPrtcmpnyInfoChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskReqstWctChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskRspnberChangeHistDto;
import aicluster.pms.api.cnvnchangehist.dto.UsptTaskTaxitmWctChangeHistDto;
import aicluster.pms.api.cnvnchangehist.service.CnvnChangeHistService;
import aicluster.pms.common.dto.CnvnChangeDto;
import aicluster.pms.common.entity.UsptCnvnChangeReqHistDt;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 협약변경내역_admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/cnvn-change-hist")
public class CnvnChangeHistController {

	@Autowired
	private CnvnChangeHistService cnvnChangeHistService;

	/****************************************협약변경내역 조회 ****************************************************/

	/**
	 * 협약변경내역 목록 조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<CnvnChangeDto> selectProcessHist(CnvnChangeHistParam cnvnChangeHistParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	selectBsnsCnvnHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectProcessHist(cnvnChangeHistParam, page);
	}

	/**
	 * 협약변경내역 목록 엑셀 다운로드
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcel(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	getListExcel : {}", cnvnChangeHistParam);
		List<CnvnChangeDto> list = cnvnChangeHistService.getListExcel(cnvnChangeHistParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("협약변경내역 목록");
		ExcelSheet<CnvnChangeDto> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	 , "접수번호"    , "과제명"      	, "사업연도"	  , "사업명"	 ,  "사업자명/이름");
		sheet.setProperties("rn"	 , "receiptNo"	, "taskNmKo"	, "bsnsYear"	  , "bsnsNm"	 , "memberNm"	   );
		sheet.setTitle("협약변경내역 목록");
		sheet.setSheetName("협약변경내역 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/****************************************협약변경내역 상세 조회 ****************************************************/
	/**
	 * 협약변경내역 변경날자 목록
	 * @param   cnvnChangeReqId(협약변경요청ID), changeIemDivCd
	 * @return
	 */
	@GetMapping("/change_de")
	public JsonList<UsptCnvnChangeReqHistDt> selectCnvnChangeReqHistDe(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectBsnsCnvnHist : {}", cnvnChangeHistParam);
		return new JsonList<>(cnvnChangeHistService.selectCnvnChangeReqHistDe(cnvnChangeHistParam));
	}

	/**
	 * 협약변경내역 기본정보 및 변경항목 조회
	 * @param   bsnsCnvnId(사업협약ID)
	 * @return
	 */
	@GetMapping("/deatail_info")
	public CnvnChangeHistBaseInfoDto selectCnvnChangeDetailInfo(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectCnvnChangeDetailInfo : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectCnvnChangeDetailInfo(cnvnChangeHistParam);
	}

	/****************************************수행기관신분변경****************************************************/
	/**
	 * 협약변경내역 수행기관신분변경
	 *  cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 */
	@GetMapping("/cnvn_sclpst_hist")
	public UsptCnvnSclpstChangeHistDto selectChangeCnvnSclpstHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeCnvnSclpstHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeCnvnSclpstHist(cnvnChangeHistParam);
	}

	/****************************************과제정보****************************************************/
	/**
	 * 협약변경내역 과제정보 상세조회
	 * cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 */
	@GetMapping("/cnvn_task_info_hist")
	public UsptCnvnTaskInfoChangeHistDto selectChangeCnvnTaskInfoHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeCnvnTaskInfoHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeCnvnTaskInfoHist(cnvnChangeHistParam);
	}

	/****************************************참여기업****************************************************/
	/**
	 * 협약변경내역 참여기업 조회
	 * cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 */
	@GetMapping("/task_prtcmpny_info_hist")
	public UsptTaskPrtcmpnyInfoChangeHistDto selectChangeTaskPrtcmpnyInfoHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeTaskPrtcmpnyInfoHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeTaskPrtcmpnyInfoHist(cnvnChangeHistParam);
	}

	/****************************************참여인력****************************************************/
	/**
	 * 협약변경내역 참여인력 조회
	 *  cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 */
	@GetMapping("/task_partcpt_hist")
	public UsptTaskPartcptsChangeHistDto selectChangeTaskPartcptsHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeTaskPartcptsHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeTaskPartcptsHist(cnvnChangeHistParam);
	}

	/****************************************사업비****************************************************/
	/**
	 * 협약변경내역 사업비 조회
	 *  cnvnChangeReqId(협약변경요청ID), changeDt(생성일시)
	 */
	@GetMapping("/task_reqst_wct_hist")
	public UsptTaskReqstWctChangeHistDto selectChangeTaskReqstWctHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeTaskReqstWctHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeTaskReqstWctHist(cnvnChangeHistParam);
	}

	/****************************************비목별사업비****************************************************/
	/**
	 * 협약변경내역 비목별사업비 조회
	 *  cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 */
	@GetMapping("/task_taxitm_wc_hist")
	public UsptTaskTaxitmWctChangeHistDto selectChangeTaskTaxitmWctHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeTaskTaxitmWctHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeTaskTaxitmWctHist(cnvnChangeHistParam);
	}

	/****************************************신청자정보****************************************************/
	/**
	 * 협약변경내역 신청자정보 조회
	 * cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 */
	@GetMapping("/cnvn_applcnt_hist")
	public UsptCnvnApplcntChangeHistDto selectChangeCnvnApplcntHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeCnvnApplcntHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeCnvnApplcntHist(cnvnChangeHistParam);
	}

	/****************************************과제책임자****************************************************/
	/**
	 * 협약변경내역 과제책임자 조회
	 * cnvnChangeReqId(협약변경요청ID) changeDt(생성일시)
	 */
	@GetMapping("/task_rspnber_hist")
	public UsptTaskRspnberChangeHistDto selectChangeTaskRspnberHist(CnvnChangeHistParam cnvnChangeHistParam){
		log.debug("#####	selectChangeTaskRspnberHist : {}", cnvnChangeHistParam);
		return cnvnChangeHistService.selectChangeTaskRspnberHist(cnvnChangeHistParam);
	}
}