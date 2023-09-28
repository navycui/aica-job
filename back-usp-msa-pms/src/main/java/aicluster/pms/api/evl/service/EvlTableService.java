package aicluster.pms.api.evl.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptEvlIemDao;
import aicluster.pms.common.dao.UsptEvlTableDao;
import aicluster.pms.common.entity.UsptEvlIem;
import aicluster.pms.common.entity.UsptEvlTable;
import aicluster.pms.common.entity.UsptEvlTableSearchParam;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlTableService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptEvlTableDao evlTableDao;

	@Autowired
	private UsptEvlIemDao evlIemDao;

	final Date now = new Date();

	/*
	 *
	 *
	 * 평가표 목록조회*/
	public CorePagination<UsptEvlTable> getList(UsptEvlTableSearchParam usptEvlTableParam, Long page) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if(page == null) {
			page = 1L;
		}

		if(usptEvlTableParam.getItemsPerPage() == null) {
			usptEvlTableParam.setItemsPerPage(ITEMS_PER_PAGE);
		}

		usptEvlTableParam.setExcel(false);
		long totalItems = evlTableDao.selectListCount(usptEvlTableParam);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptEvlTableParam.getItemsPerPage(), totalItems);

		usptEvlTableParam.setBeginRowNum(info.getBeginRowNum());
		List<UsptEvlTable> list = evlTableDao.selectList(usptEvlTableParam);

		//출력할 자료 생성 후 리턴
		CorePagination<UsptEvlTable> pagination = new CorePagination<>(info, list);

		return pagination;
	}

	/*
	 *
	 *
	 * 평가표 목록 엑셀다운로드*/
	public List<UsptEvlTable> getListExcelDwld(UsptEvlTableSearchParam usptEvlTableParam) {
		usptEvlTableParam.setExcel(true);

		return evlTableDao.selectList(usptEvlTableParam);
	}

	/*
	 *
	 *
	 * 평가표 상세조회*/
	public UsptEvlTable get(String evlTableId) {
		UsptEvlTable usptEvlTable = evlTableDao.select(evlTableId);

		if (usptEvlTable == null) {
			throw new InvalidationException("평가표 정보가 없습니다.");
		}

		//일반
		UsptEvlIem usptEvlIem = new UsptEvlIem();
		usptEvlIem.setEvlTableId(evlTableId);
		usptEvlIem.setEvlDivCd(Code.evlDiv.일반);

		List<UsptEvlIem> evlIemList = evlIemDao.selectList(usptEvlIem);
		usptEvlTable.setUsptEvlIemList(evlIemList);

		//가점
		UsptEvlIem usptEvlAddIem = new UsptEvlIem();
		usptEvlAddIem.setEvlTableId(evlTableId);
		usptEvlAddIem.setEvlDivCd(Code.evlDiv.가점);

		List<UsptEvlIem> evlIemAddList = evlIemDao.selectList(usptEvlAddIem);
		usptEvlTable.setUsptEvlAddIemList(evlIemAddList);

		//감점
		UsptEvlIem usptEvlMinusIem = new UsptEvlIem();
		usptEvlMinusIem.setEvlTableId(evlTableId);
		usptEvlMinusIem.setEvlDivCd(Code.evlDiv.감점);

		List<UsptEvlIem> evlIemMinusList = evlIemDao.selectList(usptEvlMinusIem);
		usptEvlTable.setUsptEvlMinusIemList(evlIemMinusList);

		return usptEvlTable;
	}


	//평가표 수정불가 체크
	public int getModifyEnableCnt(String evlTableId) {
		return evlTableDao.selectModifyEnableCnt(evlTableId, Code.evlSttus.대기중);
	}

	/*
	 *
	 *
	 * 평가표 기본정보 등록*/
	public UsptEvlTable add(UsptEvlTable usptEvlTable) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		if (usptEvlTable == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		InvalidationsException exs = new InvalidationsException();

		if (string.isBlank(usptEvlTable.getEvlTableNm())) {
			exs.add("evlTableNm", "평가표명을 입력하세요.");
		}

		if (string.isBlank(usptEvlTable.getEvlMthdCd())) {
			exs.add("evlMthdCd", "평가방식을 선택하세요");
		}


		if (usptEvlTable.getIndvdlzOpinionWritng() == null) {
		  exs.add("indvdlzOpinionWritng", "개별의견작성여부를 선택하세요.");
		}

		if (exs.size() > 0) {
			throw exs;
		}

		String evlTablIdInputKey = string.getNewId("evltable-");

		usptEvlTable.setEvlTableId(evlTablIdInputKey);
		usptEvlTable.setCreatorId(worker.getMemberId());
		usptEvlTable.setUpdaterId(worker.getMemberId());
		usptEvlTable.setCreatedDt(now);
		usptEvlTable.setUpdatedDt(now);

		int regCnt = evlTableDao.insert(usptEvlTable);

		if(regCnt < 1) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "등록"));
		}

		UsptEvlTable usptEvlTableResult = evlTableDao.select(evlTablIdInputKey);

		return usptEvlTableResult;
	}


	/*
	 *
	 *
	 * 평가표 수정 관리*/
	public void modify(UsptEvlTable param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		//1.기본정보 유효체크
		final List<UsptEvlIem> usptEvlIemList = param.getUsptEvlIemList();

		final List<UsptEvlIem> usptEvlAddIemList = param.getUsptEvlAddIemList();

		final List<UsptEvlIem> usptEvlMinusIemList = param.getUsptEvlMinusIemList();

		UsptEvlTable usptEvlTable = evlTableDao.select(param.getEvlTableId());

		if (usptEvlTable == null) {
			throw new InvalidationException("등록되지 않은 평가표입니다.");
		}

		if (usptEvlIemList == null) {
			//&& usptEvlAddIemList.size()  == 0 && usptEvlMinusIemList.size() == 0
			throw new InvalidationException("평가항목 구성정보를 입력하세요.");
		}

		//2.평가항목리스트 유효체크
		//일반
		if(usptEvlIemList.size() > 0) {
			regValidCheck(param, usptEvlIemList);

			//배점형인 경우 토탈점수 100이어야 함
			if( string.equals(param.getEvlMthdCd(), Code.evlMthd.배점형)){
				regValidSumCheck(usptEvlIemList);//합계점수 100 체크
			}

		}

		//가점
		if(usptEvlAddIemList != null) {
			regValidCheck(param, usptEvlAddIemList);
		}

		//감점
		if(usptEvlMinusIemList != null) {
			regValidCheck(param, usptEvlMinusIemList);
		}


		//3.평가표 기본정보 수정처리
		param.setUpdatedDt(now);
		param.setUpdaterId(worker.getMemberId());
		evlTableDao.update(param);

		//4.평가항목리스트 등록,수정,삭제 처리
		//일반
		if(usptEvlIemList != null) {
			regProcess(param, usptEvlIemList);
		}

		//가점
		if(usptEvlAddIemList != null) {
			regProcess(param, usptEvlAddIemList);
		}

		//감점
		if(usptEvlMinusIemList != null) {
			regProcess(param, usptEvlMinusIemList);
		}

	}

	public void modifyEnable(UsptEvlTable param) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		param.setUpdatedDt(now);
		param.setUpdaterId(worker.getMemberId());

		evlTableDao.update(param);
	}

	public void regValidSumCheck(List<UsptEvlIem> usptEvlIemList) {
		String _flag = "";
		int sumPoint = 0;

		for(UsptEvlIem usptEvlIem : usptEvlIemList) {
			_flag =  usptEvlIem.getFlag();

			if(string.equals(_flag, "I") || string.equals(_flag, "U") || _flag == null) {
				sumPoint += usptEvlIem.getAllotScore();
				log.debug("getAllotScore = {}", usptEvlIem.getAllotScore());
				log.debug("sumPoint = {}", sumPoint);
			}

		}

		if(sumPoint != 100) {
			throw new InvalidationException("배점 총합 점수가 100점이 아닙니다.");
		}
	}

	public void regValidCheck(UsptEvlTable param, List<UsptEvlIem> usptEvlIemList) {
		InvalidationsException exs = new InvalidationsException();

		int evlIemCount = 0;
		String _flag = "";
		for(UsptEvlIem usptEvlIem : usptEvlIemList) {
			evlIemCount ++;

			if (usptEvlIem.getSortOrdrNo() == null) {
				exs.add("sortOrdrNo", evlIemCount + "번째 행의 우선순위를 입력해주세요.");
			}

			if (string.isBlank(usptEvlIem.getEvlIemNm())) {
				exs.add("evlIemNm", evlIemCount + "번째 행의 평가항목명을 입력해주세요.");
			}

			if (string.isBlank(usptEvlIem.getEvlIemCn())) {
				exs.add("evlIemCn", evlIemCount + "번째 행의 세부항목 내용을 입력해주세요.");
			}

			if (usptEvlIem.getAllotScore() == null) {
				exs.add("allotScore", evlIemCount + "번째 행의 배점을 입력해주세요.");
			}

			if (usptEvlIem.getEvlDivCd() == null) {
				exs.add("evlDivCd", evlIemCount + "번째 행의 평가구분코드를 입력해주세요.");
			}

			//평가방식에 따른 체크 처리 -> 척도형이면 평가기준번호가 있어야한다.
			if (string.equals(param.getEvlMthdCd(), Code.evlMthd.척도형)) {
				if(usptEvlIem.getEvlStdrNo() ==  null) {
					exs.add("evlStdrNo", evlIemCount + "번째 행의 평가기준번호를 입력해주세요.");
				}
			}

			_flag =  usptEvlIem.getFlag();

			//_flag 에 따른 처리 => null이면 기존, I이면 등록, U이면 수정, D이면 삭제
			if (string.equals(_flag, "I") || string.equals(_flag, "U") || string.equals(_flag, "D") ) {
				if(string.isBlank(usptEvlIem.getEvlTableId())) {
					exs.add("evlTableId", evlIemCount + "번째 행의 평가표ID를 입력해주세요.");
				}

				if (string.equals(_flag, "U") || string.equals(_flag, "D") ) {
					if(string.isBlank(usptEvlIem.getEvlIemId())) {
						exs.add("evlIemId", evlIemCount + "번째 행의 평가항목ID를 입력해주세요.");
					}else {
						UsptEvlIem usptEvlIemInfo = evlIemDao.select(usptEvlIem.getEvlIemId());

						if (usptEvlIemInfo == null) {
							throw new InvalidationException(evlIemCount + "번째 행의 평가항목은 등록되지 않은 평가항목입니다.");
						}
					}
				}
			}
		}

		if (exs.size() > 0) {
			throw exs;
		}
	}

	public void regProcess(UsptEvlTable param, List<UsptEvlIem> usptEvlIemList) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		String _flag = "";

		for(UsptEvlIem usptEvlIem : usptEvlIemList) {
			_flag =  usptEvlIem.getFlag();

			usptEvlIem.setCreatedDt(now);
			usptEvlIem.setCreatorId(worker.getMemberId());
			usptEvlIem.setUpdatedDt(now);
			usptEvlIem.setUpdaterId(worker.getMemberId());

			if (string.equals(_flag, "I")) {
				usptEvlIem.setEvlIemId(string.getNewId("evliem-"));

				evlIemDao.insert(usptEvlIem);
			}else if (string.equals(_flag, "U")) {
				evlIemDao.update(usptEvlIem);
			}else if (string.equals(_flag, "D")) {
				evlIemDao.delete(usptEvlIem);
			}
		}
	}
}

