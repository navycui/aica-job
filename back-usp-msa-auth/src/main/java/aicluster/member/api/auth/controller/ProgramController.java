package aicluster.member.api.auth.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.member.api.auth.dto.ProgramDto;
import aicluster.member.api.auth.service.ProgramService;
import aicluster.member.common.entity.CmmtProgrm;
import bnet.library.util.dto.JsonList;

@RestController
@RequestMapping("/api/auth/programs")
public class ProgramController {

	@Autowired
	private ProgramService programService;

	/**
	 * 프로그램-역할 목록 조회
	 *
	 * @param systemId
	 * @param programNm
	 * @param urlPattern
	 * @return
	 */
	@GetMapping("")
	public JsonList<CmmtProgrm> getList(String systemId, String programNm, String urlPattern){
		return programService.getList(systemId, programNm, urlPattern);
	}

	/**
	 * 프로그램-역할 추가
	 *
	 * @param dto
	 * @return
	 */
	@PostMapping("")
	public CmmtProgrm add(@RequestBody(required = false) ProgramDto dto) {
		CmmtProgrm cmmtProgram = programService.addProgram(dto);
		return cmmtProgram;
	}

	/**
	 * 프로그램-역할 조회
	 * @param programId
	 * @return
	 */
	@GetMapping("/{programId}")
	public CmmtProgrm get(@PathVariable String programId) {
		CmmtProgrm cmmtProgram = programService.getProgram(programId);
		return cmmtProgram;
	}

	/**
	 * 프로그램-역할 수정
	 *
	 * @param programId
	 * @param dto
	 * @return
	 */
	@PutMapping("/{programId}")
	public CmmtProgrm modify(@PathVariable String programId, @RequestBody(required = false) ProgramDto dto) {
		dto.setProgramId(programId);
		return programService.modifyProgram(dto);
	}

	/**
	 * 프로그램-역할 삭제
	 *
	 * @param programId
	 */
	@DeleteMapping("/{programId}")
	public void remove(@PathVariable String programId) {
		programService.removeProgram(programId);
	}

	/**
	 * 프로그램-역할 초기데이터 적재
	 *
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws IOException
	 */
	@PostMapping("/init-data")
	public void initData() throws UnsupportedEncodingException, ParseException, IOException {
		programService.initData();
	}
}
