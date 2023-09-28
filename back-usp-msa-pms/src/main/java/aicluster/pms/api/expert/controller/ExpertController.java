package aicluster.pms.api.expert.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

import aicluster.framework.config.EnvConfig;
import aicluster.pms.api.expert.dto.ExpertDto;
import aicluster.pms.api.expert.dto.ExpertListExcelDto;
import aicluster.pms.api.expert.dto.ExpertListParam;
import aicluster.pms.api.expert.service.ExpertService;
import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertCl;
import aicluster.pms.common.util.Code;
import bnet.library.excel.ExcelReader;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문가정보관리admin
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/expert")
public class ExpertController {

	@Autowired
	private ExpertService expertService;

	@Autowired
	private EnvConfig config;

	/**
	 * 전문가정보관리 목록조회
	 * @param ExpertListParam
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptExpert> getExpertList(ExpertListParam expertListParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	getExpertReqsList : {}", expertListParam);
		return expertService.getExpertList(expertListParam, page);
	}

	/**
	 * 전문가정보관리 목록 엑셀 다운로드
	 * @param ExpertListParam
	 * @return
	 */
	@GetMapping("/excel-dwld")
	public ModelAndView getExpertListExcel(ExpertListParam expertListParam){

		log.debug("#####	getExpertListExcel : {}", expertListParam);

		List<UsptExpert> list = expertService.getExpertListExcel(expertListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전문가신청 목록");
		ExcelSheet<UsptExpert> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "이름"		, "내/외국인"	, "직장명"		, "직위");
		sheet.setProperties("rn"	, "expertNm"	, "nativeNm"	, "wrcNm"	, "ofcpsNm");
		sheet.setTitle("전문가신청 목록");
		sheet.setSheetName("전문가신청 목록");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}


	/**
	 * 전문가정보관리 상세정보
	 * @param expertId
	 * @return
	 */
	@GetMapping("/detail/{expertId}")
	public ExpertDto getExpertInfo(HttpServletRequest request, @PathVariable("expertId") String expertId) {
		log.debug("#####	getExpertInfo : {}", expertId);
		return expertService.getExpertInfo(request, expertId);
	}

	/**
	 * 전문가 변경
	 * @param expertId
	 * @param expertDto
	 * @param fileList
	 */
	@PutMapping("/modify/{expertId}")
	public ExpertDto modifyExpert( @PathVariable("expertId") String expertId
			              , @RequestPart(value = "info", required = false) ExpertDto expertDto
			              , @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {
		return expertService.modifyExpert(expertId, expertDto, fileList);
	}

	/**
	 * 전문가 삭제
	 * @param expertId
	 * @param expertListParam
	 * @return
	 */
	@DeleteMapping("")
	public void removeExpert(@RequestBody ExpertListParam expertListParam){
		  expertService.deleteExpert(expertListParam);
	}

	/**
	 * 전문가 매칭이력
	 * @param expertId
	 * @return
	 */
	@GetMapping("/hist")
	public CorePagination<UsptExpert> getExpertMatchHistList(ExpertListParam expertListParam, @RequestParam(defaultValue = "1") Long page) {
		log.debug("#####	expertListParam : {}", expertListParam);
		return expertService.getExpertMatchHistList(expertListParam, page);
	}

	/**
	 * 전문가 매칭이력엑셀 다운로드
	 * @param ExpertListParam
	 * @return
	 */
	@GetMapping("/excel-dwld/hist")
	public ModelAndView getExpertMatchHistListExcel(ExpertListParam expertListParam){

		log.debug("#####	getExpertMatchHistListExcel : {}", expertListParam);

		List<UsptExpert> list = expertService.getExpertMatchHistListExcel(expertListParam);

		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전문가 매칭이력");
		ExcelSheet<UsptExpert> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(  "번호"	, "매칭일",   "사업년도",    "사업명",		 "공고번호", 		"공고명",			"활동분양"	);
		sheet.setProperties("rn"	, "regDt",    "bsnsYear", 	"bsnsNm"	,	 "pblancNo"	,	 "pblancNm"	, 	"actRealm" );
		sheet.setTitle("전문가 매칭이력");
		sheet.setSheetName("전문가 매칭이력");
		wb.addSheet(sheet);
		return ExcelView.getView(wb);
	}

	/**
	 * 전문가 excel 등록
	 * @param
	 * @return
	 */
	@PostMapping("/excel-upld")
	public JsonList<ExpertListExcelDto> modifyExpertExcel( @RequestPart(value = "excel", required = false) MultipartFile excel) {

		if (excel == null || excel.getSize() == 0) {
			throw new InvalidationException("엑셀파일을 업로드 하세요.");
		}
		log.info("excel.getSize() ==================================1: "+excel.getSize() );
		String tempDir = config.getDir().getTempDir();
		log.info("tempDir==================================2: "+tempDir);
		// 확장자 검사
		String filename = excel.getOriginalFilename();
		String ext = CoreUtils.filename.getExtension(filename);
		if (!string.equalsIgnoreCase(ext, "xlsx")) {
			throw new InvalidationException("확장자가 'xlsx'인 엑셀파일만 가능합니다.");
		}
		log.info("ext==================================3: "+ext);
		/*
		 * 임시 디렉터리에 엑셀파일 저장
		 */
		filename = tempDir + File.separator + string.getNewId("excel-") + ".xlsx";
		File fexcel = new File( filename );
		try {
			excel.transferTo(fexcel);
			log.info("==================================4");
		} catch (IllegalStateException | IOException e) {
			throw new InvalidationException("엑셀파일을 읽을 수 없습니다.");
		}
		log.info("filename==================================5: "+filename);
		/*
		 * 엑셀 파일 읽기
		 */
		ExcelReader reader = null;
		List<ExpertListExcelDto> list = null;
		try {
			reader = new ExcelReader(fexcel);
			XSSFSheet sheet = reader.getSheet("List");
			if (sheet == null) {
				throw new InvalidationException("엑셀파일에 'List' sheet가 없습니다.");
			}
			log.info("==================================6");
			// 엑셀 컬럼을 읽어서 저장할 DTO의 Property를 엑셀 컬럼과 동일한 순서로 나열한다.
			String[] properties = {"expertClId",    "expertNm", 	"encBrthdy",	 "genderNm"	,  "nativeYn"	, 	"encMbtlnum" , "encEmail" , "wrcNm",  "ofcpsNm"};
			log.info("==================================7");
			int startRow = 1; // 0: 첫줄, 1: 2번째 줄
			list = reader.read(ExpertListExcelDto.class, sheet, properties, startRow);
			log.info("list==================================8: "+list);
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
			throw new InvalidationException("저장할 전문가 정보가 없습니다.");
		}

		log.info("==================================	modifyExpertExcel==> : {}"+list );

		int evlIemCount = 0 ;
		//유효체크
		for(ExpertListExcelDto listInfo : list) {
			evlIemCount++;

			//필수 입력 체크
			if( string.isBlank(listInfo.getExpertClId()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 전문가분류명코드는 필수 입력값입니다.");
			}
			if( string.isBlank(listInfo.getExpertNm()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 이름은 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getEncBrthdy()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 생년월일은 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getEncMbtlnum()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 휴대폰번호는 필수 입력값입니다.");
			}

			if( string.isBlank(listInfo.getEncEmail()) ){
				throw new InvalidationException(evlIemCount +"번째 행 - 이메일은 필수 입력값입니다.");
			}

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

		for(ExpertListExcelDto listInfo : list) {
			evlIemCount ++;
			int existCount = 0 ;

			//기존 등록여부 체크(이름, 생년월일, 성별, 휴대폰)
			String chkExpertNm= listInfo.getExpertNm();
			String chkBirth = listInfo.getEncBrthdy();
			String chkMbtlnum = listInfo.getEncMbtlnum();
			String chkEmail = listInfo.getEncEmail();

			for(ExpertListExcelDto listInfo2 : list) {
				String existExpertNm = listInfo2.getExpertNm();
				String existBirth = listInfo2.getEncBrthdy();
				String existMbtlnum = listInfo2.getEncMbtlnum();
				String existEmail= listInfo2.getEncEmail();

				if( string.equals(chkExpertNm, existExpertNm) &&  string.equals(chkBirth, existBirth) && string.equals(chkMbtlnum, existMbtlnum) && string.equals(chkEmail, existEmail) ) {
					existCount++;
				}

				if(existCount > 1) {
					throw new InvalidationException(evlIemCount +"번째 행의 전문가 정보가 엑셀 파일 내에 중복으로 존재합니다.\n 중복값(이름, 생년월일, 휴대폰번호, 이메일 모두 일치)");
				}
			}
		}

		for(ExpertListExcelDto listInfo : list) {
			if(string.equals(listInfo.getGenderNm(), "남성")) {
				listInfo.setGenderCd(Code.gender.남성);
			}else if(string.equals(listInfo.getGenderNm(), "여성")) {
				listInfo.setGenderCd(Code.gender.여성);
			}else if(string.equals(listInfo.getGenderNm(), "없음")) {
				listInfo.setGenderCd(Code.gender.없음);
			}
			listInfo.setEncBrthdy(listInfo.getEncBrthdy().replaceAll("-", ""));//- 없앰.
			listInfo.setEncMbtlnum(listInfo.getEncMbtlnum().replaceAll("-", ""));//- 없앰.
		}

		for(ExpertListExcelDto listInfo : list) {
			log.debug("listInfo = {}", listInfo.toString());
			listInfo.setSuccessYn("Y");
		}

//		return new JsonList<>(list);
		return new JsonList<>(expertService.modifyExpertExcel(list));
	}


	/**
	 * 전문가 등록 템플릿 엑셀 다운로드
	 * @param
	 * @return
	 */
	@GetMapping("/excel-templ")
	public ModelAndView getExpertListExcelTemp(){

		List<UsptExpert> list = new ArrayList<>();
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("전문가 등록");
		ExcelSheet<UsptExpert> sheet = new ExcelSheet<>();
		sheet.addRows(list);
		sheet.setHeaders(       "*전문가분류코드(중)",   "*이름",        "*생년월일",	 "성별", 		  "내/외국인",    "*휴대폰번호",	"*이메일"	,	"직장명",  "직위");
		sheet.setProperties(     "expertClId",    "expertNm", 	"encBrthdy",	 "gender "	,  "nativeYn"	, 	"encMbtlnum" , "encEmail" , "wrcNm",  "ofcpsNm");
//		sheet.setTitle("전문가 등록");
		sheet.setSheetName("List");
		wb.addSheet(sheet);

		//전문가 분류 코드 조회
		List<UsptExpertCl> codeList = expertService.selectExpertClfcTreeList();
		for(UsptExpertCl outParam : codeList) {
			if(string.equals(outParam.getLevel(), "2")){
				outParam.setLevel("       중");
			}else {
				outParam.setLevel("");
			}
		}

		ExcelSheet<UsptExpertCl> sheet2 = new ExcelSheet<>();
		sheet2.addRows(codeList);
		sheet2.setHeaders(   "코드"          , "코드명"        , "코드분류");
		sheet2.setProperties("expertClId"	, "expertClNm" , "level");
		sheet2.setTitle("전문가 분류 코드");
		sheet2.setSheetName("전문가 분류 코드");
		wb.addSheet(sheet2);

		return ExcelView.getView(wb);
	}

	/**
	 * 전문가 분류조회_부모전문가분류 조회
	 * @return
	 */
	@GetMapping("/expert-clid/parnts")
	public JsonList<ExpertClIdParntsDto> selectParntsExpertClIdList() {
		log.debug("#####	selectParntsExpertClIdList : {}" );
		return new JsonList<>(expertService.selectParntsExpertClIdList());
	}


	/**
	 * 전문가 분류조회_전문가분류보 조회
	 * @return
	 */
	@GetMapping("/expert-clid/{expertClId}")
	public JsonList<ExpertClIdDto> selectExpertClIdList(@PathVariable("expertClId") String expertClId) {
		return new JsonList<>(expertService.selectExpertClIdList(expertClId));
	}

}