package aicluster.pms.api.expertReqst.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.expertReqst.dto.ExpertClIdDto;
import aicluster.pms.api.expertReqst.dto.ExpertClIdParntsDto;
import aicluster.pms.api.expertReqst.dto.FrontExpertReqstCntDto;
import aicluster.pms.api.expertReqst.dto.FrontExpertReqstDto;
import aicluster.pms.api.expertReqst.service.FrontExpertReqstService;
import bnet.library.util.dto.JsonList;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문가 신청 front
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/front/expert-reqst")
public class FrontExpertReqstController {

	@Autowired
	private FrontExpertReqstService frontExpertReqstService;

	/**
	 * 전문가 신청자 유무
	 * @return
	 */
	@GetMapping("/expert-reqsting-cnt")
	public FrontExpertReqstCntDto getExpertReqstCnt() {
		return frontExpertReqstService.getExpertReqstCnt();
	}

	/**
	 * 전문가 신청자정보 조회
	 * @return
	 */
	@GetMapping("")
	public FrontExpertReqstDto getExpertReqstInfo() {
		return frontExpertReqstService.getExpertReqstInfo();
	}

	/**
	 * 저장
	 * @param frontExpertReqstDto
	 * @param fileList
	 * @param fileList
	 */
	@PostMapping("")
	public void add( @RequestPart(value = "info", required = false) FrontExpertReqstDto frontExpertReqstDto
			    		  , @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) {

		log.debug("#####	getUsptExpert : {}", "["+frontExpertReqstDto.getUsptExpert()+"]");
		log.debug("#####	fileList : {}", "["+fileList+"]");
		frontExpertReqstService.add(frontExpertReqstDto, fileList );
	}

	/**
	 * 전문가 분류조회_부모전문가분류 조회
	 * @return
	 */
	@GetMapping("/expert-clid/parnts")
	public JsonList<ExpertClIdParntsDto> selectParntsExpertClIdList() {
		return new JsonList<>(frontExpertReqstService.selectParntsExpertClIdList());
	}

	/**
	 * 전문가 분류조회_전문가분류 조회
	 * @return
	 */
	@GetMapping("/expert-clid/{expertClId}")
	public JsonList<ExpertClIdDto> selectExpertClIdList(@PathVariable("expertClId") String expertClId) {
		log.debug("#####	selectExpertClIdList : {}", expertClId);
		return new JsonList<>(frontExpertReqstService.selectExpertClIdList(expertClId));
	}

	/**
	 * 파일 단건 다운
	 * @param attachmentId
	 * @return
	 */
	@GetMapping("/atchmnfl/{attachmentId}")
	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
		log.debug("#####	getOneFileDown : {}", attachmentId);
		frontExpertReqstService.getOneFileDown(response, attachmentId);
	}

}