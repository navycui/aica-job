package aicluster.pms.api.evl.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfDownUtils;
import aicluster.framework.common.util.dto.LogIndvdInfTrgtItem;
import aicluster.framework.common.util.dto.LogIndvdlInfDown;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.api.evl.dto.EvlCmitExtrcTargetParam;
import aicluster.pms.api.evl.service.EvlCmitService;
import aicluster.pms.common.dto.EvlCmitExtrcResultDto;
import aicluster.pms.common.dto.EvlLoginCmitListDto;
import aicluster.pms.common.entity.UsptCnsltHist;
import aicluster.pms.common.entity.UsptEvlCmit;
import aicluster.pms.common.entity.UsptEvlCmitExtrcTarget;
import aicluster.pms.common.entity.UsptEvlMfcmm;
import aicluster.pms.common.entity.UsptEvlMfcmmExtrc;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertExcel;
import aicluster.pms.common.entity.UsptExpertResult;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/evl-cmit")
public class EvlCmitController {

	@Autowired
	private EvlCmitService evlCmitService;

	@Autowired
	private LogIndvdlInfDownUtils indvdlInfDownUtils;

	/**
	 * 평가위원회 추출 대상 목록조회
	 *
	 * @param usptEvlCmitExtrcTarget
	 * @param page
	 * @return
	 */
	@GetMapping("/extrc")
	public CorePagination<UsptEvlCmitExtrcTarget> getEvlCmitExtrcTargetList(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	evlCmitExtrcTargetParam : {}", evlCmitExtrcTargetParam.toString());
		return evlCmitService.getEvlCmitExtrcTargetList(evlCmitExtrcTargetParam, page);
	}


	/**
	 * 평가위원회 추출 대상 목록 엑셀 다운로드
	 *
	 * @param usptEvlCmitExtrcTarget
	 * @return
	 */
	@GetMapping("/extrc/excel-dwld")
	public ModelAndView getEvlCmitExtrcTargetListExcel(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam){
		List<UsptEvlCmitExtrcTarget> list = evlCmitService.getEvlCmitExtrcTargetListExcel(evlCmitExtrcTargetParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가위원 추출 대상 목록");
		ExcelSheet<UsptEvlCmitExtrcTarget> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "처리상태"		, "사업년도", "공고명"		, "평가위원회명", "위원수"	, "분과명"	, "평가장소", "평가유형"	, "모집유형"		, "접수차수"	, "간사");
		sheet.setProperties("rn"	, "processSttusNm"	, "bsnsYear", "pblancNm"	, "pblancNm"	, "mfcmmCo"	, "sectNm"	, "evlPlace", "evlTypeNm"	, "ordtmRcritNm"	, "rceptOdr"	, "orgnzrNm");
		sheet.setTitle("평가위원 추출 대상 목록");
		sheet.setSheetName("평가 위원 추출 대상 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 평가위원 추출 상세 조회
	 *
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}")
	public UsptEvlCmitExtrcTarget get(@PathVariable String evlCmitId) {
		return evlCmitService.get(evlCmitId);
	}


	/**
	 * 평가위원 추출
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@PostMapping("/{evlCmitId}/evl-cmit-extrc")
	public EvlCmitExtrcResultDto add(@PathVariable String evlCmitId, @RequestBody UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		log.debug("#####	evlCmitId : {}", evlCmitId);

		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("추출할 조건 정보가 없습니다.");
		}

		log.debug("#####	usptEvlMfcmmExtrc : {}", usptEvlMfcmmExtrc.toString());

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		UsptEvlMfcmmExtrc usptEvlMfcmmExtrcResult = evlCmitService.add(usptEvlMfcmmExtrc);

		return evlCmitService.getEvlCmitExtrcResult(usptEvlMfcmmExtrcResult);

	}


	/**
	 * 평가위원 추출 차수별 상세 조회
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-cmit-extrc")
	public EvlCmitExtrcResultDto getEvlCmitExtrcResult(@PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {

		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("조회 조건 정보가 없습니다.");
		}

		if (usptEvlMfcmmExtrc.getOdrNo() == null) {
			usptEvlMfcmmExtrc.setOdrNo(1);//차수가 없으면 1로 세팅
		}

		log.debug("#####	usptEvlMfcmmExtrc : {}", usptEvlMfcmmExtrc.toString());

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		return evlCmitService.getEvlCmitExtrcResult(usptEvlMfcmmExtrc);
	}

	/**
	 * 평가위원 추출 차수별 상세 조회- 추출위원 엑셀 다운로드
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-cmit-extrc/excel-dwld")
	public ModelAndView getEvlCmitExtrcExcel(HttpServletRequest request, @PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("조회 조건 정보가 없습니다.");
		}

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		if (usptEvlMfcmmExtrc.getOdrNo() == null) {
			usptEvlMfcmmExtrc.setOdrNo(1);//차수가 없으면 1로 세팅
		}

		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
			throw new InvalidationException("엑셀다운 작업내용을 입력해주세요");
		}

		List<UsptExpertExcel> list = evlCmitService.getEvlCmitExtrcExcelResult(usptEvlMfcmmExtrc).getExtrcExpertExcelList();

		if(list.size() < 1) {
			throw new InvalidationException("다운로드할 정보가 없습니다.");
		}


		List<LogIndvdInfTrgtItem> logTrgt = new ArrayList<>();
		for (UsptExpertExcel excelDto : list) {
			LogIndvdInfTrgtItem trgtItem = new LogIndvdInfTrgtItem();
			trgtItem.setTrgterId(excelDto.getExpertId());
			trgtItem.setTrgterNm(excelDto.getExpertNm());
			trgtItem.setEmail(excelDto.getEmail());
			trgtItem.setBirthday("");
			trgtItem.setMobileNo(excelDto.getMbtlnum());
			logTrgt.add(trgtItem);
		}

//		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
//			usptEvlMfcmmExtrc.setExcelDwldWorkCn("평가위원 추출 목록 엑셀다운로드 작업");
//		}

		LogIndvdlInfDown logParam = LogIndvdlInfDown.builder()
				.memberId(worker.getMemberId())
				.memberIp(webutils.getRemoteIp(request))
				.workTypeNm("엑셀다운로드")
				.workCn(usptEvlMfcmmExtrc.getExcelDwldWorkCn())
				.menuId("menu-ADM030201")
				.menuNm("평가위원 추출")
				.fileNm("평가위원 추출 목록.xlsx")
				.trgtIdList(logTrgt)
				.build();
		indvdlInfDownUtils.insertList(logParam);


		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가위원 추출 목록");
		ExcelSheet<UsptExpertExcel> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "추출차수",	"전문분야"	, "이름"	, "소속기관"	, "부서명"	, "직위명"	, "이메일"		, "휴대전화"	);
		sheet.setProperties("rn"	, "odrNm"	,	"expertClNm"	, "expertNm", "wrcNm"		, "deptNm"	, "ofcpsNm"	, "email"	, "mbtlnum"	);
		sheet.setTitle("추출평가위원 목록");
		sheet.setSheetName("추출평가위원 목록");
		wb.addSheet(sheet);

		return ExcelView.getView(wb);
	}

	/**
	 * 평가위원 추출 차수별 상세 조회- 제외위원 엑셀 다운로드
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/evl-cmit-excl/excel-dwld")
	public ModelAndView getEvlCmitExclExcel(HttpServletRequest request, @PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("조회 조건 정보가 없습니다.");
		}

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		if (usptEvlMfcmmExtrc.getOdrNo() == null) {
			usptEvlMfcmmExtrc.setOdrNo(1);//차수가 없으면 1로 세팅
		}

		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
			throw new InvalidationException("엑셀다운 작업내용을 입력해주세요");
		}

		List<UsptExpertExcel> list = evlCmitService.getEvlCmitExtrcExcelResult(usptEvlMfcmmExtrc).getExclExpertExcelList();

		if(list.size() < 1) {
			throw new InvalidationException("다운로드할 정보가 없습니다.");
		}

		List<LogIndvdInfTrgtItem> logTrgt = new ArrayList<>();
		for (UsptExpertExcel excelDto : list) {
			LogIndvdInfTrgtItem trgtItem = new LogIndvdInfTrgtItem();
			trgtItem.setTrgterId(excelDto.getExpertId());
			trgtItem.setTrgterNm(excelDto.getExpertNm());
			trgtItem.setEmail(excelDto.getEmail());
			trgtItem.setBirthday("");
			trgtItem.setMobileNo(excelDto.getMbtlnum());
			logTrgt.add(trgtItem);
		}

//		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
//			usptEvlMfcmmExtrc.setExcelDwldWorkCn("평가위원 제외위원 목록 엑셀다운로드 작업");
//		}

		LogIndvdlInfDown logParam = LogIndvdlInfDown.builder()
				.memberId(worker.getMemberId())
				.memberIp(webutils.getRemoteIp(request))
				.workTypeNm("엑셀다운로드")
				.workCn(usptEvlMfcmmExtrc.getExcelDwldWorkCn())
				.menuId("menu-ADM030201")
				.menuNm("평가위원 추출")
				.fileNm("평가위원 제외위원 목록.xlsx")
				.trgtIdList(logTrgt)
				.build();
		indvdlInfDownUtils.insertList(logParam);


		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가위원 제외위원 목록");
		ExcelSheet<UsptExpertExcel> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "추출차수",	"제외조건"	, "이름"	, "소속기관"	, "부서명"	, "직위명"	, "이메일"		, "휴대전화"	);
		sheet.setProperties("rn"	, "odrNm"	,	"exclResnCn"	, "expertNm", "wrcNm"		, "deptNm"	, "ofcpsNm"	, "email"	, "mbtlnum"	);
		sheet.setTitle("제외위원 목록");
		sheet.setSheetName("제외위원 목록");
		wb.addSheet(sheet);

		return ExcelView.getView(wb);
	}


	/**
	 * 평가위원 섭외 대상 목록조회
	 *
	 * @param usptEvlCmitExtrcTarget
	 * @param page
	 * @return
	 */
	@GetMapping("/lsn")
	public CorePagination<UsptEvlCmitExtrcTarget> getEvlCmitLsnTargetList(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	evlCmitExtrcTargetParam : {}", evlCmitExtrcTargetParam.toString());
		return evlCmitService.getEvlCmitExtrcTargetList(evlCmitExtrcTargetParam, page);
	}


	/**
	 * 평가위원회 섭외 대상 목록 엑셀 다운로드
	 *
	 * @param usptEvlCmitExtrcTarget
	 * @return
	 */
	@GetMapping("/lsn/excel-dwld")
	public ModelAndView getEvlCmitLsnTargetListExcel(EvlCmitExtrcTargetParam evlCmitExtrcTargetParam){
		List<UsptEvlCmitExtrcTarget> list = evlCmitService.getEvlCmitExtrcTargetListExcel(evlCmitExtrcTargetParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가위원 추출 대상 목록");
		ExcelSheet<UsptEvlCmitExtrcTarget> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "사업년도", "공고명"		, "평가위원회명", "위원수"	, "분과명"	, "평가장소", "평가유형"	, "모집유형"		, "접수차수"	, "간사");
		sheet.setProperties("rn"	, "bsnsYear", "pblancNm"	, "pblancNm"	, "mfcmmCo"	, "sectNm"	, "evlPlace", "evlTypeNm"	, "ordtmRcritNm"	, "rceptOdr"	, "orgnzrNm");
		sheet.setTitle("평가위원 섭외 대상 목록");
		sheet.setSheetName("평가 위원 섭외 대상 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 평가위원  섭외 상세 조회
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/lsn")
	public UsptEvlCmitExtrcTarget getLsn(@PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("조회 조건 정보가 없습니다.");
		}

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		if (usptEvlMfcmmExtrc.getOdrNo() == null) {
			usptEvlMfcmmExtrc.setOdrNo(1);//차수가 없으면 1로 세팅
		}

		log.debug("#####	evlCmitId : {}", evlCmitId.toString());
		log.debug("#####	usptEvlMfcmmExtrc : {}", usptEvlMfcmmExtrc.toString());

		return evlCmitService.getLsn(usptEvlMfcmmExtrc);
	}


	/**
	 * 평가위원 섭외 상세 - 평가위원목록
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/lsn-list")
	public JsonList<UsptExpert> getLsnMfcmmList(@PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc) {
		if (usptEvlMfcmmExtrc == null) {
			throw new InvalidationException("조회 조건 정보가 없습니다.");
		}

		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		if (usptEvlMfcmmExtrc.getOdrNo() == null) {
			usptEvlMfcmmExtrc.setOdrNo(1);//차수가 없으면 1로 세팅
		}

		log.debug("#####	evlCmitId : {}", evlCmitId.toString());
		log.debug("#####	usptEvlMfcmmExtrc : {}", usptEvlMfcmmExtrc.toString());

		return new JsonList<>(evlCmitService.getLsnMfcmmList(usptEvlMfcmmExtrc));
	}

	/**
	 * 평가위원 섭외 상세 - 평가위원목록 엑셀 다운로드
	 *
	 * @param evlCmitId
	 * @param usptEvlMfcmmExtrc
	 * @return
	 */
	@GetMapping("/{evlCmitId}/lsn-list/excel-dwld")
	public ModelAndView getLsnMfcmmListExcel(HttpServletRequest request, @PathVariable String evlCmitId, UsptEvlMfcmmExtrc usptEvlMfcmmExtrc){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		usptEvlMfcmmExtrc.setEvlCmitId(evlCmitId);

		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
			throw new InvalidationException("엑셀다운 작업내용을 입력해주세요");
		}

		List<UsptExpertExcel> list = evlCmitService.getLsnMfcmmExcelList(usptEvlMfcmmExtrc);

		if(list.size() < 1) {
			throw new InvalidationException("다운로드할 정보가 없습니다.");
		}



		//로그 저장
		List<LogIndvdInfTrgtItem> logTrgt = new ArrayList<>();
		for (UsptExpertExcel excelDto : list) {
			LogIndvdInfTrgtItem trgtItem = new LogIndvdInfTrgtItem();
			trgtItem.setTrgterId(excelDto.getExpertId());
			trgtItem.setTrgterNm(excelDto.getExpertNm());
			trgtItem.setEmail(excelDto.getEmail());
			trgtItem.setBirthday("");
			trgtItem.setMobileNo(excelDto.getMbtlnum());
			logTrgt.add(trgtItem);
		}

//		if(CoreUtils.string.isBlank(usptEvlMfcmmExtrc.getExcelDwldWorkCn())) {
//			usptEvlMfcmmExtrc.setExcelDwldWorkCn("평가위원 섭외 상세 평가위원 목록 엑셀다운로드 작업");
//		}

		LogIndvdlInfDown logParam = LogIndvdlInfDown.builder()
				.memberId(worker.getMemberId())
				.memberIp(webutils.getRemoteIp(request))
				.workTypeNm("엑셀다운로드")
				.workCn(usptEvlMfcmmExtrc.getExcelDwldWorkCn())
				.menuId("menu-ADM030202")
				.menuNm("평가위원 섭외")
				.fileNm("평가위원 섭외 상세 평가위원 목록.xlsx")
				.trgtIdList(logTrgt)
				.build();
		indvdlInfDownUtils.insertList(logParam);

		//엑셀 다운 처리
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("평가위원 섭외 상세 평가위원 목록");
		ExcelSheet<UsptExpertExcel> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "추출차수",	"전문분야"	, "이름"	, "소속기관"	, "부서명"	, "직위명"	, "이메일"		, "휴대전화"	, "처리상태");
		sheet.setProperties("rn"	, "odrNm"	,	"expertClNm"	, "expertNm", "wrcNm"		, "deptNm"	, "ofcpsNm"	, "email"	, "mbtlnum"	, "lsnResultNm");
		sheet.setTitle("평가위원 섭외 상세 - 추출 차수별 전문위원 목록");
		sheet.setSheetName(usptEvlMfcmmExtrc.getOdrNo() + "차 추출 전문위원 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 평가위원 상세 정보(섭외 결과 팝업창 상세조회용)
	 *
	 * @param extrcMfcmmId
	 * @return
	 */
	@GetMapping("/mfcmm-detail/{extrcMfcmmId}")
	public UsptExpertResult getMfcmmDetail(HttpServletRequest request, @PathVariable String extrcMfcmmId) {
		return evlCmitService.getMfcmmDetail(request, extrcMfcmmId);
	}


	/**
	 * 평가위원 섭외 결과 등록
	 *
	 * @param extrcMfcmmId
	 * @param usptCnsltHist
	 * @return
	 */
	@PutMapping("/mfcmm-detail/{extrcMfcmmId}")
	public UsptCnsltHist modifyMfcmmDetail(@PathVariable String extrcMfcmmId,  @RequestBody UsptCnsltHist usptCnsltHist) {
		log.debug("#####	extrcMfcmmId : {}", extrcMfcmmId);
		log.debug("#####	usptCnsltHist : {}", usptCnsltHist.toString());

		usptCnsltHist.setExtrcMfcmmId(extrcMfcmmId);

		return evlCmitService.modifyMfcmmDetail(usptCnsltHist);
	}


	/**
	 * 평가위원 상담이력 목록
	 *
	 * @param extrcMfcmmId
	 * @return
	 */
	@GetMapping("/mfcmm-cnslt-hist/{extrcMfcmmId}")
	public JsonList<UsptCnsltHist> getMfcmmCnsltHistList(@PathVariable String extrcMfcmmId) {
		return new JsonList<>(evlCmitService.getMfcmmCnsltHistList(extrcMfcmmId));
	}

	/**
	 * 평가위원 추출 상세 조회
	 *
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/{evlCmitId}/basic-info")
	public UsptEvlCmit getEvlCmitInfo(@PathVariable String evlCmitId) {
		return evlCmitService.getEvlCmitInfo(evlCmitId);
	}


	/**
	 * 종합의견 등록/수정
	 *
	 * @param evlCmitId
	 * @param usptEvlCmit
	 * @return
	 */
	@PutMapping("/{evlCmitId}/charmn-opinion")
	public UsptEvlCmit addCharmnOpinion(@PathVariable String evlCmitId, @RequestBody UsptEvlCmit usptEvlCmit) {
		log.debug("#####	evlCmitId : {}", evlCmitId);
		log.debug("#####	usptEvlCmit : {}", usptEvlCmit.toString());

		usptEvlCmit.setEvlCmitId(evlCmitId);

		return evlCmitService.addCharmnOpinion(usptEvlCmit);
	}

	/**
	 * 평가처리 완료(위원회)
	 *
	 * @param evlCmitId
	 * @return
	 */
	@PutMapping("/{evlCmitId}/evl-compt")
	public UsptEvlCmit modifyEvlComptByCmit(@PathVariable String evlCmitId) {
		log.debug("#####	evlCmitId : {}", evlCmitId);

		return evlCmitService.modifyEvlCompt(evlCmitId);
	}


	/**
	 * 평가처리 완료 - 취소
	 *
	 * @param evlCmitId
	 * @return
	 */
	@PutMapping("/{evlCmitId}/evl-compt-cancle")
	public UsptEvlCmit modifyEvlComptCancle(@PathVariable String evlCmitId) {
		log.debug("#####	evlCmitId : {}", evlCmitId);

		return evlCmitService.modifyEvlComptCancle(evlCmitId);
	}


	/**
	 * 평가처리 완료(위원)
	 *
	 * @param evlMfcmmId
	 * @return
	 */
	@PutMapping("/evl-compt-mfcmm/{evlMfcmmId}")
	public UsptEvlMfcmm modifyEvlComptByEvlMfcmm(@PathVariable String evlMfcmmId) {
		log.debug("#####	evlMfcmmId : {}", evlMfcmmId);

		return evlCmitService.modifyEvlComptByEvlMfcmm(evlMfcmmId);
	}


	/**
	 * 로그인가능한 평가위원회 목록
	 *
	 * @return
	 */
	@GetMapping("/login-cmit")
	public JsonList<EvlLoginCmitListDto> getLoginCmitList() {
		return new JsonList<>(evlCmitService.getLoginCmitList());
	}

}



