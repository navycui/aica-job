package aicluster.common.api.terms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aicluster.common.api.terms.dto.TermsParam;
import aicluster.common.api.terms.service.TermsService;
import aicluster.common.common.dto.TermsListItem;
import aicluster.common.common.dto.TermsDetailDto;
import aicluster.common.common.entity.CmmtStplat;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/terms")
public class TermsController {

	@Autowired
	private TermsService service;

	/**
	 * 약관 시행일 목록조회
	 *
	 * @param termsType
	 * @param srchCd
	 * @return
	 */
	@GetMapping("/{termsType}")
	public JsonList<TermsListItem> getDayList(@PathVariable String termsType, @RequestParam String srchCd) {
		srchCd = string.upperCase(srchCd);

		log.debug("srchCd : ["+srchCd+"]");
		return service.getDayList(termsType, srchCd);
	}

	/**
	 * 약관 등록
	 *
	 * @param termsType
	 * @param cmmtTerms
	 * @return
	 */
	@PostMapping("/{termsType}")
	public TermsDetailDto add(@PathVariable String termsType, @RequestBody TermsParam param) {
		param.setTermsType(termsType);
		return service.insert(param);
	}

	/**
	 * 약관 상세조회
	 *
	 * @param termsType
	 * @param beginDay
	 * @return
	 */
	@GetMapping("/{termsType}/{beginDay}")
	public TermsDetailDto get(@PathVariable String termsType, @PathVariable Date beginDay) {
		return service.get(termsType, date.format(beginDay, "yyyyMMdd"));
	}

	/**
	 * 약관 수정
	 *
	 * @param termsType
	 * @param beginDay
	 * @param cmmtTerms
	 * @return
	 */
	@PutMapping("/{termsType}/{beginDay}")
	public TermsDetailDto modify(@PathVariable String termsType, @PathVariable Date beginDay,@RequestBody TermsParam param) {
		param.setTermsType(termsType);
		param.setBeginDay(beginDay);

		return service.modify(param);
	}

	/**
	 * 약관 삭제
	 *
	 * @param termsType
	 * @param beginDay
	 */
	@DeleteMapping("/{termsType}/{beginDay}")
	public void remove(@PathVariable String termsType,@PathVariable Date beginDay) {
		service.remove(termsType, date.format(beginDay, "yyyyMMdd"));
	}

	/**
	 * 오늘 시행 약관 조회
	 * @param termsType
	 * @return
	 */
	@GetMapping("/{termsType}/now")
	public JsonList<CmmtStplat> getToday(@PathVariable String termsType) {
		return service.getTodayTerms(termsType);
	}

}
