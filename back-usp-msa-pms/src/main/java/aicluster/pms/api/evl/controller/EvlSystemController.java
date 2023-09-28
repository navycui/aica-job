package aicluster.pms.api.evl.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.evl.dto.EvlSystemParam;
import aicluster.pms.api.evl.dto.EvlSystemTableDto;
import aicluster.pms.api.evl.dto.EvlTableDto;
import aicluster.pms.api.evl.dto.EvlTableParam;
import aicluster.pms.api.evl.service.EvlSystemService;
import aicluster.pms.common.dto.EvlSttusListDto;
import aicluster.pms.common.dto.EvlSystemDto;
import aicluster.pms.common.dto.EvlSystemLogInDto;
import aicluster.pms.common.dto.EvlSystemTargetDto;
import bnet.library.util.dto.JsonList;
import lombok.extern.slf4j.Slf4j;

/**
 * 평가시스템 - 온라인 평가
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/evl-system")
public class EvlSystemController {

	@Autowired
	private EvlSystemService evlSystemService;


	/**
	 * 평가시스템 login 평가위원회ID 조회
	 * @param
	 * @return
	 */
	@GetMapping("/evl-evlcmitId")
	public JsonList<EvlSystemLogInDto> getEvlCmitId(EvlSystemParam evlSystemParam){
		log.debug("#####	getEvlCmitId : {}", evlSystemParam);
		return new JsonList<>(evlSystemService.getEvlCmitId(evlSystemParam));
	}

	/**
	 * 평가위원 정보 조회
	 * @param
	 * @return
	 */
	@GetMapping("/evl-mfcmm-info")
	public EvlSystemDto getEvMfcmmInfo(EvlSystemParam evlSystemParam){
		log.debug("#####	getEvMfcmmInfo : {}", evlSystemParam);
		return evlSystemService.getEvMfcmmInfo(evlSystemParam);
	}

	/**
	 * 평가위원 평가장 입장 - 출석체크
	 * @param  chklstChkAt
	 * @return
	 */
	@PutMapping("/atend-sttus/save")
	public void modifyAtendSttus(@RequestBody(required = false) EvlSystemParam evlSystemParam){
		log.debug("#####	modifyAtendSttus : {}", evlSystemParam);
		evlSystemService.modifyAtendSttus(evlSystemParam);
	}

	/**
	 * 평가위원 동의 및 퇴장 - 회피동의여부
	 * @param  chklstChkAt
	 * @return
	 */
	@PutMapping("/evas-agre-at/save")
	public void modifyEvasAgreAt(@RequestBody(required = false) EvlSystemParam evlSystemParam){
		log.debug("#####	modifyEvasAgreAt : {}", evlSystemParam);
		evlSystemService.modifyEvasAgreAt(evlSystemParam);
	}

	/**
	 * 온라인 평가대상 목록 조회
	 * @param evlCmitId
	 * @return
	 */
	@GetMapping("/evl-cmit-targetlist")
	public JsonList<EvlSystemTargetDto> getEvlSystemTargetList(EvlSystemParam evlSystemParam){
		log.debug("#####	getEvlSystemTargetList : {}", evlSystemParam);
		return new JsonList<>(evlSystemService.getEvlSystemTargetList(evlSystemParam));
	}

	/**
	 * 온라인 평가대상 목록 - 평가자료 다운(발표자료)
	 * @param
	 * @return
	 */
	@GetMapping("/evl-cmit-targetlist/grp-file-dwln")
	public void getEvlAllFileDown(HttpServletResponse response, EvlSystemParam evlSystemParam){
		log.debug("#####	getEvlAllFileDown : {}", evlSystemParam);
		evlSystemService.getEvlAllFileDown(response, evlSystemParam);
	}


//	/**
//	 * 온라인 평가대상 목록 - 평가자료 다운(평가표)
//	 * @param
//	 * @return
//	 */
//	@GetMapping("/evl-cmit-targetlist/evl-table-dwln")
//	public EvlTableDto getEvlTableDown(EvlTableParam evlTableParam){
//		return evlSystemService.getEvlTable(evlTableParam);
//	}

	/**
	 * 평가표 조회
	 * @param evlCmitId, evlTrgetId
	 * @return
	 */
	@GetMapping("/evl-table")
	public EvlTableDto getEvlTable(EvlTableParam param) {
		return evlSystemService.getEvlTable(param);
	}

	/**
	 * 평가표 등록-임시저장
	 * @param evl_mfcmm_id, evlTrgetId evl_iem_id
	 * @return
	 */
	@PutMapping("/evl-table/save")
	public void modifyEvlTable(@RequestBody(required = false) EvlSystemTableDto evlSystemTableDto){
		log.debug("#####	modifyEvlTable : {}", evlSystemTableDto);
		evlSystemService.modifyEvlTable(evlSystemTableDto);
	}

	/**
	 * -평가완료(일반위원)
	 * @param evlResultId, evlTrgetId, evlOpinion
	 * @return
	 */
	@PutMapping("/evl-table/complete")
	public void modifyEvlTableComplete(@RequestBody(required = false) EvlSystemParam evlSystemParam){
		log.debug("#####	modifyEvlTableComplete : {}", evlSystemParam);
		evlSystemService.modifyEvlTableComplete(evlSystemParam);
	}

	/**
	 * 평가완료-위원장 평가완료 여부 및 위원평가 여부 조회
	 * @param evlCmitId, evlTrgetId
	 * @return
	 */
	@GetMapping("/evl-charmn-opinion/completeYn/{evlCmitId}")
	public EvlSystemParam getCharmnEvlSttus(@PathVariable("evlCmitId") String evlCmitId) {
		log.debug("#####	getCharmnEvlSttus : {}", evlCmitId);
		return evlSystemService.getCharmnEvlSttus(evlCmitId);
	}


	/**
	 * 평가완료-위원장 종합의견(결과현황조회)
	 * @param evlCmitId, evlTrgetId
	 * @return
	 */
	@GetMapping("/evl-charmn-opinion/{evlCmitId}")
	public JsonList<EvlSttusListDto> getEvlSttusList(@PathVariable("evlCmitId") String evlCmitId) {
		return new JsonList<>(evlSystemService.getEvlSttusList(evlCmitId));
	}

	/**
	 *평가완료- 위원장 평가표 등록
	 * @param evlCmitId ,evlCharmnOpinionCn
	 * @return
	 */
	@PutMapping("/evl-charmn-opinion/complete")
	public void modifyEvlCharmnComplete(@RequestBody(required = false) EvlSystemParam evlSystemParam){
		log.debug("#####	modifyEvlCharmnComplete : {}", evlSystemParam);
		evlSystemService.modifyEvlCharmnComplete(evlSystemParam);
	}

}
