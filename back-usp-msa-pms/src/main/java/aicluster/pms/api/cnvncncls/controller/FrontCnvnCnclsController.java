package aicluster.pms.api.cnvncncls.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aicluster.pms.api.cnvncncls.dto.CnvnCnclsParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsDto;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsInputParam;
import aicluster.pms.api.cnvncncls.dto.FrontCnvnCnclsParam;
import aicluster.pms.api.cnvncncls.service.FrontCnvnCnclsService;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import bnet.library.util.pagination.CorePagination;
import lombok.extern.slf4j.Slf4j;

/**
 * 전자협약 관리_front
 * @author brainednet
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/front/cnvn-cncls")
public class FrontCnvnCnclsController {

	@Autowired
	private FrontCnvnCnclsService frontCnvnCnclsService;


	/**
	 * 전자협약 관리 목록조회
	 * @return
	 */
	@GetMapping("")
	public CorePagination<UsptBsnsCnvn> getList(FrontCnvnCnclsParam frontCnvnCnclsParam, @RequestParam(defaultValue = "1") Long page){
		log.debug("#####	frontCnvnCnclsParam : {}", frontCnvnCnclsParam);
		return frontCnvnCnclsService.getList(frontCnvnCnclsParam, page );
	}

	/**
	 * 전자협약 관리 상세 조회
	 * @param frontCnvnCnclsParam
	 * @return
	 */
	@GetMapping("/detail-info")
	public FrontCnvnCnclsDto getBsnsCnvnInfo(FrontCnvnCnclsParam frontCnvnCnclsParam){
		log.debug("#####	getBsnsPlanDocInfo : {}", frontCnvnCnclsParam);
		return frontCnvnCnclsService.getBsnsCnvnInfo(frontCnvnCnclsParam);
	}

	/**
	 * 전자협약 참여기업 서명
	 * @param BsnsPlanParam
	 * @return
	 */
	@PutMapping("/sign")
	public void modifyPrtcmpnySign (@RequestBody FrontCnvnCnclsInputParam frontCnvnCnclsInputParam) {

		log.debug("#####	modifyPrtcmpnySign : {}", frontCnvnCnclsInputParam);
		frontCnvnCnclsService.modifyPrtcmpnySign(frontCnvnCnclsInputParam );
	}

	/**
	 *  전자협약 관리 협약서 다운 정보 조회
	 * @param frontBsnsPlanParam
	 * @return
	 */
	@GetMapping("/cnclsDoc")
	public FrontCnvnCnclsDto getCnclsDocInfo(FrontCnvnCnclsParam frontCnvnCnclsParam){
		log.debug("#####	getFileDown : {}", frontCnvnCnclsParam);
		return frontCnvnCnclsService.getCnclsDocInfo(frontCnvnCnclsParam);
	}

	/**
	 * 파일 단건 다운 2022.11.15 삭제
	 * @param attachmentId
	 * @return
	 */
//	@GetMapping("/atchmnfl/{attachmentId}")
//	public void getOneFileDown(HttpServletResponse response, @PathVariable("attachmentId") String attachmentId){
//		log.debug("#####	getOneFileDown : {}", attachmentId);
//		frontCnvnCnclsService.getOneFileDown(response, attachmentId);
//	}

}