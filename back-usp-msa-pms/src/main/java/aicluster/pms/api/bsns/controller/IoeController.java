package aicluster.pms.api.bsns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.pms.api.bsns.service.IoeService;
import aicluster.pms.common.dto.WctIoeDto;
import bnet.library.util.dto.JsonList;

/**
 * 비목관리
 * @author brainednet
 *
 */
@RestController
@RequestMapping("/api/ioe")
public class IoeController {

	@Autowired
	private IoeService ioeSerive;

	/**
	 * 목록 조회
	 * @return
	 */
	@GetMapping("")
	public JsonList<WctIoeDto> getList(){
		return ioeSerive.getList();
	}

	/**
	 * 수정
	 * @param ioeList
	 * @return
	 */
	@PutMapping("")
	public JsonList<WctIoeDto> modify(@RequestBody List<WctIoeDto> ioeList){
		ioeSerive.modify(ioeList);
		return ioeSerive.getList();
	}
}
