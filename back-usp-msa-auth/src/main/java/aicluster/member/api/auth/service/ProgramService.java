package aicluster.member.api.auth.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.auth.dto.ProgramDto;
import aicluster.member.common.dao.CmmtProgrmDao;
import aicluster.member.common.dao.CmmtProgrmRoleDao;
import aicluster.member.common.entity.CmmtProgrm;
import aicluster.member.common.entity.CmmtProgrmRole;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProgramService {

	@Autowired
	private CmmtProgrmDao cmmtProgramDao;

	@Autowired
	private CmmtProgrmRoleDao cmmtProgramRoleDao;

	public JsonList<CmmtProgrm> getList(String systemId, String programNm, String urlPattern) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		if (string.isBlank(systemId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "시스템ID"));
		}

		List<CmmtProgrm> list = cmmtProgramDao.selectList(systemId, programNm, urlPattern);

		return new JsonList<>(list);
	}

	public CmmtProgrm addProgram(ProgramDto dto) {

		Date now = new Date();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력 값 검증
		if (string.isBlank(dto.getProgramNm())) {
			inputValidateErrs.add("programNm", String.format(validateMessageExt.입력없음, "프로그램 이름"));
		}

		if (string.isBlank(dto.getSystemId())) {
			inputValidateErrs.add("systemId", String.format(validateMessageExt.입력없음, "시스템ID"));
		}

		if (string.isBlank(dto.getHttpMethod())) {
			inputValidateErrs.add("httpMethod", String.format(validateMessageExt.입력없음, "Method 이름"));
		}

		if (string.isBlank(dto.getUrlPattern())) {
			inputValidateErrs.add("urlPattern", String.format(validateMessageExt.입력없음, "URL Pattern"));
		}

		if (dto.getCheckOrder() == null) {
			inputValidateErrs.add("checkOrder", String.format(validateMessageExt.입력없음, "checkorder"));
		}

		if (dto.getRoles() == null || dto.getRoles().length == 0) {
			inputValidateErrs.add("roles", String.format(validateMessageExt.입력없음, "역할"));
		}

		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// programNm 중복 조회
		CmmtProgrm cmmtProgram = cmmtProgramDao.selectByName(dto.getSystemId(), dto.getProgramNm());
		if (cmmtProgram != null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "프로그램명"));
		}

		// urlPattern 중복 조회
		cmmtProgram = cmmtProgramDao.selectByUrlPattern(dto.getSystemId(), dto.getHttpMethod(), dto.getUrlPattern());
		if (cmmtProgram != null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "url pattern"));
		}

		// program ID 생성
		String programId = CoreUtils.string.getNewId("program-");

		// checkOrder 값 비교
		cmmtProgramDao.incCheckOrder(dto.getCheckOrder());

		// 입력값 세팅
		cmmtProgram = CmmtProgrm.builder()
				.programId(programId)
				.programNm(dto.getProgramNm())
				.systemId(dto.getSystemId())
				.httpMethod(dto.getHttpMethod())
				.urlPattern(dto.getUrlPattern())
				.checkOrder(dto.getCheckOrder())
				.createdDt(now)
				.creatorId(worker.getMemberId())
				.updatedDt(now)
				.updaterId(worker.getMemberId())
				.build();

		// 프로그램 정보 입력
		cmmtProgramDao.insert(cmmtProgram);

		// 리스트 생성
		List<CmmtProgrmRole> list = new ArrayList<>();
		for (String role : dto.getRoles()) {
			CmmtProgrmRole cmmtProgramRole;
			cmmtProgramRole = CmmtProgrmRole.builder()
					.programId(programId)
					.roleId(role)
					.createdDt(cmmtProgram.getCreatedDt())
					.creatorId(cmmtProgram.getCreatorId())
					.build();
			list.add(cmmtProgramRole);
		}

		// 리스트 정보 입력
		cmmtProgramRoleDao.insertList(list);

		CmmtProgrm program = cmmtProgramDao.select(programId);

		// 정보 조회
		return program;
	}

	public CmmtProgrm getProgram(String programId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력 값 검증
		if (string.isBlank(programId)) {
			throw new InvalidationException("프로그램 ID를 입력하세요.");
		}

		CmmtProgrm cmmtProgram = cmmtProgramDao.select(programId);
		if (cmmtProgram == null) {
			throw new InvalidationException("자료가 없습니다.");
		}

		return cmmtProgram;
	}

	public CmmtProgrm modifyProgram(ProgramDto dto) {

		Date now = new Date();
		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력 값 검증
		if (string.isBlank(dto.getProgramNm())) {
			inputValidateErrs.add("programNm", String.format(validateMessageExt.입력없음, "프로그램 이름"));
		}

		if (string.isBlank(dto.getSystemId())) {
			inputValidateErrs.add("systemId", String.format(validateMessageExt.입력없음, "시스템ID"));
		}

		if (string.isBlank(dto.getHttpMethod())) {
			inputValidateErrs.add("httpMethod", String.format(validateMessageExt.입력없음, "Method 이름"));
		}

		if (string.isBlank(dto.getUrlPattern())) {
			inputValidateErrs.add("urlPattern", String.format(validateMessageExt.입력없음, "URL Pattern"));
		}

		if (dto.getCheckOrder() == null) {
			inputValidateErrs.add("checkOrder", String.format(validateMessageExt.입력없음, "checkorder"));
		}

		if (dto.getRoles() == null) {
			inputValidateErrs.add("roles", String.format(validateMessageExt.입력없음, "역할"));
		}

		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 중복 확인
		CmmtProgrm cmmtProgram = cmmtProgramDao.select(dto.getProgramId());
		if (cmmtProgram == null) {
			throw new InvalidationException("자료가 없습니다.");
		}

		// programNm 중복 조회
		cmmtProgram = cmmtProgramDao.selectByName(dto.getSystemId(), dto.getProgramNm());
		if (cmmtProgram != null) {
			if (!string.equals(dto.getProgramId(), cmmtProgram.getProgramId())) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "프로그램명"));
			}
		}

		// urlPattern 중복 조회(현재 수정할 programId와 다른 programId가 있는 경우)
		cmmtProgram = cmmtProgramDao.selectByUrlPattern(dto.getSystemId(), dto.getHttpMethod(), dto.getUrlPattern());
		if (cmmtProgram != null) {
			if ( !string.equals(cmmtProgram.getProgramId(), dto.getProgramId()) ) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "url pattern"));
			}
		}

		cmmtProgramDao.incCheckOrder(dto.getCheckOrder());

		cmmtProgram = cmmtProgramDao.select(dto.getProgramId());

		cmmtProgram.setProgramNm(dto.getProgramNm());
		cmmtProgram.setHttpMethod(dto.getHttpMethod());
		cmmtProgram.setUrlPattern(dto.getUrlPattern());
		cmmtProgram.setCheckOrder(dto.getCheckOrder());
		cmmtProgram.setUpdatedDt(now);
		cmmtProgram.setUpdaterId(worker.getMemberId());

		cmmtProgramDao.update(cmmtProgram);

		cmmtProgramRoleDao.deleteProgramId(dto.getProgramId());

		// 리스트 생성
		List<CmmtProgrmRole> list = new ArrayList<>();
		for (String role : dto.getRoles()) {
			CmmtProgrmRole cmmtProgramRole;

			cmmtProgramRole = CmmtProgrmRole.builder()
					.programId(dto.getProgramId())
					.roleId(role)
					.createdDt(cmmtProgram.getCreatedDt())
					.creatorId(cmmtProgram.getCreatorId())
					.build();

			list.add(cmmtProgramRole);
		}

		// 리스트 정보 입력
		cmmtProgramRoleDao.insertList(list);

		CmmtProgrm program = cmmtProgramDao.select(dto.getProgramId());

		return program;
	}

	public void removeProgram(String programId) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력 값 검증
		if (string.isBlank(programId)) {
			throw new InvalidationException("프로그램 ID를 입력하세요.");
		}

		CmmtProgrm cmmtProgram = cmmtProgramDao.select(programId);
		if (cmmtProgram == null) {
			throw new InvalidationException("자료가 없습니다.");
		}

		cmmtProgramRoleDao.deleteProgramId(programId);

		cmmtProgramDao.deleteProgramId(programId);

	}

	/**
	 * CMMT_PROGRAM 테이블에 초기 Data 적재
	 * (resources에 생성된 json/cmmt_program.json 파일을 기반으로 초기 데이터를 테이블에 적재한다.)
	 *
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws IOException
	 */
	public void initData() throws UnsupportedEncodingException, ParseException, IOException
	{
		// 내부사용자 검증
		SecurityUtils.checkWorkerIsInsider();

		// 데이터 유무 검증
		long cnt = cmmtProgramDao.selectTotalCount();
		if (cnt > 0) {
			throw new InvalidationException("자료가 존재합니다.");
		}

		// resources 내에 존재하는 cmmt_program.json 파일 읽기
		ObjectMapper mapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource("json/cmmt_program.json");
		if (!resource.exists()) {
			throw new InvalidationException("초기 데이터 JSON 파일이 없습니다.");
		}

		// json Data를 Object에 답기
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ProgramDto.class);
		List<ProgramDto> programList = mapper.readValue(resource.getFile(), listType);
		//log.info(programList.toString());

		// 프로그램-역할 초기데이터 테이블 insert
		int insCnt = 0;
		for (ProgramDto program : programList)
		{
			program.setProgramId(string.getNewId("program-"));
			program.setCheckOrder(1L);

			// 시스템ID가 미정의된 경우 "PORTAL_COMMON"으로 정의
			if (string.isBlank(program.getSystemId())) {
				program.setSystemId("PORTAL_COMMON");
			}

			log.info(String.format("[%d]번재 적재 데이터 : %s", (insCnt+1), program.toString()));

			CmmtProgrm cmmtProgram = addProgram(program);
			if (cmmtProgram != null) {
				insCnt++;
			}
		}

		log.info(String.format("프로그램 등록 건수 : [%d]", insCnt));
	}

}
