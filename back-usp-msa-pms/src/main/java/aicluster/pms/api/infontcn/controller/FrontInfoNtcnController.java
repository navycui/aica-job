package aicluster.pms.api.infontcn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.infontcn.service.FrontInfoNtcnService;
import aicluster.pms.common.dto.FrontBsnsPblancListDto;
import aicluster.pms.common.dto.FrontInfoNtcnEduListDto;
import aicluster.pms.common.dto.InfoNtcnDto;
import aicluster.pms.common.dto.MyBsnsNtcnListDto;
import aicluster.pms.common.entity.UsptBsnsInfoNtcn;
import bnet.library.util.dto.JsonList;

/**
 * 정보알림
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/front/info-ntcn")
public class FrontInfoNtcnController {

	@Autowired
	private FrontInfoNtcnService frontInfoNtcnService;

	/**
	 * 목록 조회
	 * @return
	 */
	@GetMapping("")
	public JsonList<InfoNtcnDto> getList() {
		return frontInfoNtcnService.getList();
	}

	/**
	 * 저장
	 * @param infoNtcnList
	 * @return
	 */
	@PostMapping("")
	public JsonList<InfoNtcnDto> add(@RequestBody List<UsptBsnsInfoNtcn> infoNtcnList) {
		frontInfoNtcnService.add(infoNtcnList);
		return frontInfoNtcnService.getList();
	}

	/**
	 * 삭제
	 * @return
	 */
	@DeleteMapping("")
	public JsonList<InfoNtcnDto> remove(){
		frontInfoNtcnService.remove();
		return frontInfoNtcnService.getList();
	}


	/**
	 * 사업공고 목록 조회
	 * @return
	 */
	@GetMapping("/bsrList")
	public JsonList<FrontBsnsPblancListDto> getPblancList() {
		return frontInfoNtcnService.getPblancList();
	}

	/**
	 * 교육 목록 조회
	 * @return
	 */
	@GetMapping("/eduList")
	public JsonList<FrontInfoNtcnEduListDto> getEduList() {
		return frontInfoNtcnService.getEduList();
	}


	/**
	 * 나의 사업관리 알림
	 * @return
	 */
	@GetMapping("/bsns-ntcn")
	public JsonList<MyBsnsNtcnListDto> getMyBsnsNtcnList(){
		return new JsonList<>(frontInfoNtcnService.getMyBsnsNtcnList());
	}
}
