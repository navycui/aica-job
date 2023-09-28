package aicluster.pms.api.evl.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.config.EnvConfig;
import aicluster.pms.api.evl.service.EvlPlanService;
import aicluster.pms.common.entity.UsptBsnsPblancRcept;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlCmitDta;
import aicluster.pms.common.entity.UsptEvlPlan;
import aicluster.pms.common.entity.UsptEvlStep;
import aicluster.pms.common.entity.UsptEvlTrget;
import aicluster.pms.common.entity.UsptSect;
import aicluster.pms.common.entity.UsptUdstdprcp;
import aicluster.pms.common.util.Code;
import bnet.library.excel.ExcelReader;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.excel.report.ExcelReportProvider;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import bnet.library.view.ReportExcelView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/evl-plans")
public class EvlPlanController {

	@Autowired
	private EvlPlanService evlPlanService;

	@Autowired
	private EnvConfig config;

	/*평가계획 목록조회*/
	@GetMapping("")
	public CorePagination<UsptEvlPlan> getList(String evlSttusCd, String evlTypeCd , Boolean ordtmRcrit, String pblancNm, String pblancNo, String chargerNm, @RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "10") Long itemsPerPage) {
		//진행상태, 모집유형, 평가유형, 공고명, 공고번호, 담당자명
		//log.debug("#####	usptEvlPlanParam : {}", usptEvlPlanParam);
        UsptEvlPlan usptEvlPlanParam = new UsptEvlPlan();

        usptEvlPlanParam.setEvlSttusCd(evlSttusCd);
        usptEvlPlanParam.setEvlTypeCd(evlTypeCd);
        usptEvlPlanParam.setOrdtmRcrit(ordtmRcrit);
        usptEvlPlanParam.setPblancNm(pblancNm);
        usptEvlPlanParam.setPblancNo(pblancNo);
        usptEvlPlanParam.setChargerNm(chargerNm);
        usptEvlPlanParam.setItemsPerPage(itemsPerPage);

		return evlPlanService.getList(usptEvlPlanParam, page);
	}


	/*평가계획 목록 엑셀다운로드*/
	@GetMapping("/excel-dwld")
	public ModelAndView getListExcelDwld(String evlSttusCd, String evlTypeCd , Boolean ordtmRcrit, String pblancNm, String pblancNo, String chargerNm){
		UsptEvlPlan usptEvlPlanParam = new UsptEvlPlan();

		usptEvlPlanParam.setEvlSttusCd(evlSttusCd);
		usptEvlPlanParam.setEvlTypeCd(evlTypeCd);
		usptEvlPlanParam.setOrdtmRcrit(ordtmRcrit);
		usptEvlPlanParam.setPblancNm(pblancNm);
		usptEvlPlanParam.setPblancNo(pblancNo);
		usptEvlPlanParam.setChargerNm(chargerNm);

		List<UsptEvlPlan> list = evlPlanService.getListExcelDwld(usptEvlPlanParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가계획 목록");
		ExcelSheet<UsptEvlPlan> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders("번호", "진행상태", "평가예정일시", "평가유형", "공고명", "모집유형", "접수차수", "담당부서", "담당자명", "등록일");
		sheet.setProperties("rn", "evlSttusNm", "evlPrarnde", "evlTypeNm", "pblancNm", "ordtmRcritNm", "rceptOdrNm", "chrgDeptNm", "chargerNm", "rgsde");
		sheet.setTitle("평가계획 목록");
		sheet.setSheetName("평가계획 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	//평가계획 기본정보 등록
	@PostMapping("")
	public UsptEvlPlan add(@RequestBody UsptEvlPlan usptEvlPlan) {
		log.debug("UsptEvlPlan insert");
		String evlPlanId = evlPlanService.insert(usptEvlPlan);
		return evlPlanService.get(evlPlanId);
	}


	//평가계획 수정
	@PutMapping("/{evlPlanId}")
	public UsptEvlPlan modify(@PathVariable String evlPlanId, @RequestBody UsptEvlPlan usptEvlPlan) {
		usptEvlPlan.setEvlPlanId(evlPlanId);
		evlPlanService.modify(usptEvlPlan);

		return evlPlanService.get(evlPlanId);
	}
	//평가계획 사용여부 수정
	@PutMapping("/{evlPlanId}/enable")
	public UsptEvlPlan modifyEnable(@PathVariable String evlPlanId, @RequestBody UsptEvlPlan usptEvlPlan) {
		usptEvlPlan.setEvlPlanId(evlPlanId);
		evlPlanService.modifyEnable(usptEvlPlan);

		return evlPlanService.get(evlPlanId);
	}


	//평가계획 상세조회
	@GetMapping("/{evlPlanId}")
	public UsptEvlPlan get(@PathVariable String evlPlanId) {
		return evlPlanService.get(evlPlanId);
	}


	//평가단계 목록조회
	@GetMapping("/{evlPlanId}/evl-step")
	public JsonList<UsptEvlStep> getEvlStepList(@PathVariable("evlPlanId") String evlPlanId) {
		return new JsonList<>(evlPlanService.getEvlStepList(evlPlanId));
	}


	//평가단계 저장
	@PutMapping("/{evlPlanId}/evl-step")
	public JsonList<UsptEvlStep> modifyEvlStep(@PathVariable String evlPlanId, @RequestBody List<UsptEvlStep> usptEvlStepList) {

		if (usptEvlStepList.size() < 1 ) {
			throw new InvalidationException("저장할 평가단계 정보가 없습니다.");
		}

		return new JsonList<>(evlPlanService.modifyEvlStepInfo(evlPlanId, usptEvlStepList));
	}


	//평가위원회 목록조회
	@GetMapping("/{evlPlanId}/evl-cmit")
	public JsonList<UsptEvlCmit> getEvlCmitList(@PathVariable("evlPlanId") String evlPlanId) {
		return new JsonList<>(evlPlanService.getEvlCmitList(evlPlanId));
	}


	//평가위원회 저장
	@PutMapping("/{evlPlanId}/evl-cmit")
	public JsonList<UsptEvlCmit> modifyEvlCmit(@PathVariable String evlPlanId, @RequestBody List<UsptEvlCmit> usptEvlCmitList) {

		if (usptEvlCmitList.size() < 1 ) {
			throw new InvalidationException("저장할 평가위원회 정보가 없습니다.");
		}

		return new JsonList<>(evlPlanService.modifyEvlCmitInfo(evlPlanId, usptEvlCmitList));
	}


	//이해관계자 목록조회
	@GetMapping("/{evlPlanId}/evl-udstdprcp")
	public JsonList<UsptUdstdprcp> getEvlUdstdprcpList(@PathVariable("evlPlanId") String evlPlanId) {
		return new JsonList<>(evlPlanService.getEvlUdstdprcpList(evlPlanId));
	}


	//이해관계자 저장->삭제기능만
	@PutMapping("/{evlPlanId}/evl-udstdprcp")
	public JsonList<UsptUdstdprcp> modifyEvlUdstdprcp(@PathVariable String evlPlanId, @RequestBody List<UsptUdstdprcp> usptUdstdprcpList) {
		return new JsonList<>(evlPlanService.modifyEvlUdstdprcp(evlPlanId, usptUdstdprcpList));
	}


	//이해관계자 개별등록
	@PostMapping("/{evlPlanId}/evl-udstdprcp")
	public UsptUdstdprcp addEvlUdstdprcp(@PathVariable String evlPlanId, @RequestBody(required = false) UsptUdstdprcp usptUdstdprcp) {
		if (usptUdstdprcp == null) {
			throw new InvalidationException("저장할 이해관계자 정보가 없습니다.");
		}

		usptUdstdprcp.setEvlPlanId(evlPlanId);
		return evlPlanService.addEvlUdstdprcp(usptUdstdprcp);
	}


	//평가계획 평가자료 목록조회
	@GetMapping("/evl-cmit-dta/{evlStepId}")
	public JsonList<UsptEvlCmitDta> getEvlCmitDtaList(@PathVariable("evlStepId") String evlStepId) {
		return new JsonList<>(evlPlanService.getEvlCmitDtaList(evlStepId));
	}

	//평가계획 평가자료 단건 다운로드
	@GetMapping("/evl-cmit-dta/{evlStepId}/atchmnfl-dwld")
	public void downloadStepAtchmnfl(HttpServletResponse response, @PathVariable("evlStepId") String evlStepId, CmmtAtchmnfl info) {
		evlPlanService.downloadStepAtchmnfl(response, evlStepId, info);
	}

	//평가계획 평가자료 일괄 다운로드
	@GetMapping("/evl-cmit-dta/{evlStepId}/atchmnfl-dwld-all")
	public void downloadStepAllAtchmnfl(HttpServletResponse response, @PathVariable("evlStepId") String evlStepId) {
		evlPlanService.downloadStepAllAtchmnfl(response, evlStepId);
	}


	//평가계획 평가자료 저장
	@PostMapping("/evl-cmit-dta/{evlStepId}/{evlTrgetId}")
	public UsptEvlCmitDta modifyEvlCmitDta(@PathVariable("evlStepId") String evlStepId, @PathVariable("evlTrgetId") String evlTrgetId, @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {
		log.debug("#####	evlStepId : {}", evlStepId);
		log.debug("#####	evlTrgetId : {}", evlTrgetId);

		return evlPlanService.modifyEvlCmitDta(evlStepId, evlTrgetId, attachments);
	}


	//평가계획 평가대상 분과 목록
	@GetMapping("/{evlPlanId}/evl-sect")
	public JsonList<UsptSect> getEvlSectList(@PathVariable("evlPlanId") String evlPlanId) {
		return new JsonList<>(evlPlanService.getEvlSectList(evlPlanId));
	}


	//평가계획 평가대상 불러오기 목록
	@GetMapping("/evl-target")
	public CorePagination<UsptEvlTrget> getEvlTargetList(UsptEvlTrget usptEvlTrget, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	usptEvlTrget.toString : {}", usptEvlTrget.toString());

		return evlPlanService.getEvlTargetist(usptEvlTrget, page);
	}


	//평가계획 평가대상 분과별 대상목록
	@GetMapping("/{evlPlanId}/evl-target-sect")
	public CorePagination<UsptEvlTrget> getEvlTargetBySectList(@PathVariable("evlPlanId") String evlPlanId, UsptEvlTrget usptEvlTrget, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	evlPlanId : {}", evlPlanId);
		usptEvlTrget.setEvlPlanId(evlPlanId);

		return evlPlanService.getEvlTargetBySectList(usptEvlTrget, page);
	}


	//평가계획 평가대상 분과별 대상 저장
	@PutMapping("/{evlPlanId}/evl-target-sect")
	public CorePagination<UsptEvlTrget> modifyEvlTargetBySectList(@PathVariable("evlPlanId") String evlPlanId, @RequestBody(required = false) List<UsptEvlTrget> usptEvlTrgetList){
		log.debug("#####	usptEvlTrgetList.size() : {}", usptEvlTrgetList.size());

		if (usptEvlTrgetList.size() < 1) {
			throw new InvalidationException("저장할 대상 정보가 없습니다.");
		}
		evlPlanService.modifyEvlTargetBySectList(evlPlanId, usptEvlTrgetList);

		//재조회 처리
		UsptEvlTrget param = new UsptEvlTrget();
		param.setEvlPlanId(evlPlanId);
		param.setSectId(usptEvlTrgetList.get(0).getSectId());

		return evlPlanService.getEvlTargetBySectList(param, 1L);
	}


	@GetMapping("/evl-udstdprcp/form")
	public ModelAndView form(String evlPlanId) {
		// 이해관계자 전체 조회
		List<UsptUdstdprcp> list = evlPlanService.getEvlUdstdprcpList(evlPlanId);

		for(UsptUdstdprcp listInfo : list) {
			if( !string.isBlank(listInfo.getEncBrthdy()) && listInfo.getEncBrthdy().length() >= 10) {
				listInfo.setEncBrthdy(String.format("%s-%s-%s", listInfo.getBrthdy().substring(0,4), listInfo.getBrthdy().substring(4,6), listInfo.getBrthdy().substring(6,8)));
			}

			if(!string.isBlank(listInfo.getMbtlnum())) {
				listInfo.setEncMbtlnum(listInfo.getMbtlnum());
			}
		}

		// Excel 설정
		Workbook workbook = ExcelReportProvider.excelReport()
				.custom()
				.templatePath("/templete/templete-udstdprcp2.xlsx") // src/main/resources/report/common-code.xlsx
				.targetCell("List!A1")                // 엑셀 메모 위치
				.addData("list", list)                // 데이터
				.getWorkbook();

		return ReportExcelView.modelAndView(workbook, "이해관계자 엑셀템플릿");  // ".xlsx" 파일명으로 엑셀 다운로드
	}


	//이해관계자 엑셀업로드
	@PostMapping("/{evlPlanId}/evl-udstdprcp/upload")
	public JsonList<UsptUdstdprcp> upload(@RequestPart(value = "excel", required = false) MultipartFile excel, @PathVariable("evlPlanId") String evlPlanId) {
		log.debug("#####	evlPlanId : {}", evlPlanId);

		if (excel == null || excel.getSize() == 0) {
			throw new InvalidationException("엑셀파일을 업로드 하세요.");
		}

		String tempDir = config.getDir().getTempDir();

		// 확장자 검사
		String filename = excel.getOriginalFilename();
		String ext = CoreUtils.filename.getExtension(filename);
		if (!string.equalsIgnoreCase(ext, "xlsx")) {
			throw new InvalidationException("확장자가 'xlsx'인 엑셀파일만 가능합니다.");
		}

		/*
		 * 임시 디렉터리에 엑셀파일 저장
		 */
		filename = tempDir + File.separator + string.getNewId("excel-") + ".xlsx";
		File fexcel = new File( filename );
		try {
			excel.transferTo(fexcel);
		} catch (IllegalStateException | IOException e) {
			throw new InvalidationException("엑셀파일을 읽을 수 없습니다.");
		}

		/*
		 * 엑셀 파일 읽기
		 */
		ExcelReader reader = null;
		List<UsptUdstdprcp> list = null;
		try {
			reader = new ExcelReader(fexcel);
			XSSFSheet sheet = reader.getSheet("List");
			if (sheet == null) {
				throw new InvalidationException("엑셀파일에 'List' sheet가 없습니다.");
			}

			// 엑셀 컬럼을 읽어서 저장할 DTO의 Property를 엑셀 컬럼과 동일한 순서로 나열한다.
			String[] properties = {"nm", "encBrthdy", "genderNm", "encMbtlnum", "insttNm", "bizrno", "lastSchul", "subjct"};
			int startRow = 1; // 0: 첫줄, 1: 2번째 줄
			list = reader.read(UsptUdstdprcp.class, sheet, properties, startRow);
		} catch (InvalidFormatException | IOException | InstantiationException | IllegalAccessException e) {
			throw new InvalidationException("엑셀파일을 읽을 수 없습니다.");
		} finally {
			if (reader != null) {
				reader.close();
			}
			// 임시파일 삭제
			fexcel.delete();
		}

		if (list == null) {
			list = new ArrayList<>();
		}

		if (list.size() < 1 || list == null) {
			throw new InvalidationException("저장할 이해관계자 정보가 없습니다.");
		}

		int evlIemCount = 0 ;
		//유효체크
		for(UsptUdstdprcp listInfo : list) {
			evlIemCount++;

			log.debug("listInfo.getNm() ="+listInfo.getNm());
			log.debug("listInfo.getEncBrthdy() ="+listInfo.getEncBrthdy());

			//필수 입력 체크
			if( string.isBlank(listInfo.getNm()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 이름은 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getEncBrthdy()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 생년월일은 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getEncMbtlnum()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 휴대폰번호는 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getInsttNm()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 기업(관)명은 필수 입력값입니다.");
			}

//			if( string.isBlank(listInfo.getBizrno()) ){
//				throw new InvalidationException(evlIemCount +"번째 행 - 사업자번호는 필수 입력값입니다.");
//			}

			if(!string.equals(listInfo.getGenderNm(), "남성")
				&& !string.equals(listInfo.getGenderNm(), "여성")
				&& !string.equals(listInfo.getGenderNm(), "없음")) {

				log.debug("listInfo.getGenderNm() = {}", listInfo.getGenderNm());

				throw new InvalidationException(evlIemCount +"번째 행 - 성별 정보보에는 '남성', '여성', '없음' 만 입력 가능합니다.");
			}

			String bizrno = listInfo.getBizrno().replaceAll("-", "");

			if(!CoreUtils.validation.isCompanyNumber(bizrno)) {
				throw new InvalidationException(evlIemCount +"번째 행 - 올바르지 않은 사업자번호입니다.");
			}

			log.debug("listInfo.getEncMbtlnum().substring(0,2) = "+listInfo.getEncMbtlnum().substring(0,2));

			if( !string.equals(listInfo.getEncMbtlnum().substring(0,2), "01") || !string.isNumeric(listInfo.getEncMbtlnum().replace("-","")) || listInfo.getEncMbtlnum().replace("-","").length() > 11  || listInfo.getEncMbtlnum().replace("-","").length() < 10 ){
				throw new InvalidationException(evlIemCount +"번째 행 - 휴대폰번호의 형식이 올바르지 않습니다.");
			}

			log.debug("listInfo.getEncBrthdy().replace(\"-\",\"\") = "+ listInfo.getEncBrthdy().replace("-",""));
			log.debug("string.getNumberOnly(listInfo.getEncBrthdy()) = " + string.getNumberOnly(listInfo.getEncBrthdy()));

			if( !string.isNumeric(listInfo.getEncBrthdy().replace("-","")) || string.getNumberOnly(listInfo.getEncBrthdy()).length() != 8 ){
				throw new InvalidationException(evlIemCount +"번째 행 - 생년월일의 형식이 올바르지 않습니다.");
			}

		}

		//엑셀 파일 내의 중복건 체크
		evlIemCount = 0 ;

		for(UsptUdstdprcp listInfo : list) {
			evlIemCount ++;
			int existCount = 0 ;

			//기존 등록여부 체크(이름, 생년월일, 성별, 휴대폰)
			String chkNm = listInfo.getNm();
			String chkBirth = listInfo.getEncBrthdy();
			String chkGender = listInfo.getGenderNm();
			String chkMbtlnum = listInfo.getEncMbtlnum();

			for(UsptUdstdprcp listInfo2 : list) {
				String existNm = listInfo2.getNm();
				String existBirth = listInfo2.getEncBrthdy();
				String existGender = listInfo2.getGenderNm();
				String existMbtlnum = listInfo2.getEncMbtlnum();

				if( string.equals(chkNm, existNm) &&  string.equals(chkBirth, existBirth) && string.equals(chkGender, existGender) && string.equals(chkMbtlnum, existMbtlnum) ) {
					existCount++;
				}

				if(existCount > 1) {
					throw new InvalidationException(evlIemCount +"번째 행의 이해관계자 정보가 엑셀 파일 내에 중복으로 존재합니다.\n 중복값(이름, 생년월일, 성별, 휴대폰번호 모두 일치)");
				}
			}
		}


		for(UsptUdstdprcp listInfo : list) {
			if(string.equals(listInfo.getGenderNm(), "남성")) {
				listInfo.setGenderCd(Code.gender.남성);
			}else if(string.equals(listInfo.getGenderNm(), "여성")) {
				listInfo.setGenderCd(Code.gender.여성);
			}else if(string.equals(listInfo.getGenderNm(), "없음")) {
				listInfo.setGenderCd(Code.gender.없음);
			}

			listInfo.setEncBrthdy(listInfo.getEncBrthdy().replaceAll("-", ""));//- 없앰.
			listInfo.setEncMbtlnum(listInfo.getEncMbtlnum().replaceAll("-", ""));//- 없앰.
			listInfo.setBizrno(listInfo.getBizrno().replaceAll("-", ""));//사업자번호만 - 없앰.
		}

		for(UsptUdstdprcp listInfo : list) {
			log.debug("listInfo = {}", listInfo.toString());
		}

		return new JsonList<>(evlPlanService.modifyEvlUdstdprcpByExcel(evlPlanId, list));
	}


	//공고 선택 팝업 차수 목록
	@GetMapping("/rcept-odr/{pblancId}")
	public CorePagination<UsptBsnsPblancRcept> getEvlTargetList(String pblancId, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	pblancId : {}", pblancId);

		UsptBsnsPblancRcept usptBsnsPblancRcept = new UsptBsnsPblancRcept();
		usptBsnsPblancRcept.setPblancId(pblancId);

		return evlPlanService.getPblancRcepttist(usptBsnsPblancRcept, page);
	}


}