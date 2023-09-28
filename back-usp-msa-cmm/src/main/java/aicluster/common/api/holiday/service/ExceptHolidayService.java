package aicluster.common.api.holiday.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.common.dao.CmmtRestdeExclDao;
import aicluster.common.common.entity.CmmtRestdeExcl;
import aicluster.common.common.util.CodeExt.validateMessageExt;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

@Service
public class ExceptHolidayService {

	@Autowired
	private CmmtRestdeExclDao cmmtHldyExclDao;

	public CmmtRestdeExcl addExctHoliday(CmmtRestdeExcl exctHoliday) {

		if (exctHoliday == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(exctHoliday.getYmd())) {
			errs.add("ymd", String.format(validateMessageExt.입력없음, "날짜"));
		} else {
			Date day = string.toDate(exctHoliday.getYmd());
			if (day == null) {
				errs.add("ymd", String.format(validateMessageExt.유효오류, "날짜"));
			}
			String ymd = date.format(day, "yyyyMMdd");
			exctHoliday.setYmd(ymd);
		}

		if (string.isBlank(exctHoliday.getYmdNm())) {
			errs.add("ymdNm", String.format(validateMessageExt.입력없음, "날짜명"));
		}

		if (errs.size() > 0) {
			throw errs;
		}

		CmmtRestdeExcl cmmtHldyExcl = cmmtHldyExclDao.select(exctHoliday.getYmd());
		if (cmmtHldyExcl != null) {
			throw new InvalidationException("휴일 제외 정보가 이미 등록되어 있습니다.");
		}

		// 입력값 세팅
		cmmtHldyExcl = CmmtRestdeExcl.builder()
				.ymd(exctHoliday.getYmd())
				.ymdNm(exctHoliday.getYmdNm())
				.exclReason(exctHoliday.getExclReason())
				.build();

		// 입력값 저장
		cmmtHldyExclDao.insert(exctHoliday);

		cmmtHldyExcl = cmmtHldyExclDao.select(cmmtHldyExcl.getYmd());

		return cmmtHldyExcl;
	}

	public CmmtRestdeExcl getExctHoliday(String ymd) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(ymd)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "날짜"));
		}

		CmmtRestdeExcl cmmtHldyExcl = cmmtHldyExclDao.select(ymd);
		if (cmmtHldyExcl == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "휴일 제외 정보"));
		}

		return cmmtHldyExcl;
	}

	public CmmtRestdeExcl modifyExctHoliday(CmmtRestdeExcl exctHoliday) {

		InvalidationsException errs = new InvalidationsException();

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(exctHoliday.getYmd())) {
			errs.add("ymd", String.format(validateMessageExt.입력없음, "날짜"));
		}
		else {
			Date dt = string.toDate(exctHoliday.getYmd());
			if (dt == null) {
				errs.add("ymd", String.format(validateMessageExt.입력오류, "날짜"));
			}
			String ymd = date.format(dt, "yyyyMMdd");
			exctHoliday.setYmd(ymd);
		}

		if (string.isBlank(exctHoliday.getYmdNm())) {
			errs.add("ymdNm", String.format(validateMessageExt.입력없음, "날짜명"));
		}

		if (errs.size() > 0) {
			throw errs;
		}

		CmmtRestdeExcl cmmtHldyExcl = cmmtHldyExclDao.select(exctHoliday.getYmd());
		if (cmmtHldyExcl == null) {
			throw new InvalidationException("휴일 제외 정보가 없습니다.");
		}

		// 입력값 세팅
		cmmtHldyExcl.setYmdNm(exctHoliday.getYmdNm());
		cmmtHldyExcl.setExclReason(exctHoliday.getExclReason());

		// 입력값 업데이트
		cmmtHldyExclDao.update(cmmtHldyExcl);

		cmmtHldyExcl = cmmtHldyExclDao.select(cmmtHldyExcl.getYmd());

		return cmmtHldyExcl;
	}

	public void removeExctHoliday(String ymd) {

		// 로그인 여부 검사, 내부사용자 여부 검사
		SecurityUtils.checkWorkerIsInsider();

		// 입력값 검증
		if (string.isBlank(ymd)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "날짜"));
		}

		Date dt = string.toDate(ymd);
		if (dt == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력오류, "날짜"));
		}
		ymd = date.format(dt, "yyyyMMdd");

		CmmtRestdeExcl cmmtHldyExcl = cmmtHldyExclDao.select(ymd);
		if (cmmtHldyExcl == null) {
			throw new InvalidationException("휴일 정보가 없습니다.");
		}

		cmmtHldyExclDao.delete(ymd);

	}

}
