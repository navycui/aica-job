package aicluster.batch.api.holiday.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.api.holiday.dto.ErrorResponse;
import aicluster.batch.api.holiday.dto.HolidayItem;
import aicluster.batch.api.holiday.dto.HolidayResponse;
import aicluster.batch.common.config.PublicDataConfig;
import aicluster.batch.common.dao.CmmtRestdeDao;
import aicluster.batch.common.entity.CmmtRestde;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.exception;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.xml;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HolidayService {
	@Autowired
	private PublicDataConfig publicDataConfig;

	@Autowired
	private CmmtRestdeDao cmmtHldyDao;

    private List<HolidayItem> getHolidayList(String solYear, String solMonth) {
    	String url = publicDataConfig.getUrl();
    	HttpResponse<String> res = Unirest.get(url)
    			.queryString("serviceKey", publicDataConfig.getServiceKey())
    			.queryString("solYear", solYear)
    			.queryString("solMonth", solMonth)
    			.asString();

    	if (res.getStatus() != 200) {
    		throw new LoggableException("공공데이터포털과 통신 중 오류가 발생하여 작업을 중단하였습니다.");
    	}
    	String body = res.getBody();
    	log.info(body);
    	try {
    		if (string.startsWith(body, "<OpenAPI_ServiceResponse>")) {
    			ErrorResponse er = xml.unmarshal(body, ErrorResponse.class);
    			throw new LoggableException("공공데이터포털 통신 오류(" + er.getHeader().getReturnAuthMsg() + ")");
    		}

			HolidayResponse holiday = xml.unmarshal(body, HolidayResponse.class);
			if (!string.equals("00", holiday.getHeader().getResultCode())) {
				throw new LoggableException("공공데이터포털 통신 오류(" + holiday.getHeader().getResultMsg() + ")");
			}
			log.debug(holiday.toString());
			return holiday.getBody().getItems();
		} catch (JAXBException e) {
			log.error(exception.getStackTraceString(e));
			log.error(body);
			throw new LoggableException("공공데이터포털로부터 받은 데이터 분석 중 오류가 발생하여 작업을 중단하였습니다.");
		}
    }

    public int updateHolidays(int beginYear, int beginMonth, int monthCnt) {
    	String beginYm = String.format("%04d%02d", beginYear, beginMonth);
    	Date beginDt = string.toDate(beginYm);
    	if (beginDt == null) {
    		throw new LoggableException("시작년/시작월이 올바르지 않습니다.");
    	}

    	/*
    	 * Unirest timeout 재조정
    	 */
    	Unirest.config().reset();
    	Unirest.config().connectTimeout(1000 * 60 * 3);
    	Unirest.config().socketTimeout(1000 * 60 * 3);

    	/*
    	 * 휴일 조회 및 저장
    	 */
    	String year = null;
    	String month = null;
    	List<CmmtRestde> holidayList = new ArrayList<>();
    	List<HolidayItem> holidayItemList = null;
    	CmmtRestde cmmtHldy = null;
    	Date dt = null;
    	int totalItems = 0;
    	for (int i = 0; i < monthCnt; i++) {
    		// 휴일 조회
    		dt = date.addMonths(beginDt, i);
    		year = date.format(dt, "yyyy");
    		month = date.format(dt, "MM");
    		holidayItemList = getHolidayList(year, month);
    		if (holidayItemList == null || holidayItemList.size() == 0) {
    			continue;
    		}

    		holidayList.clear();
    		for (HolidayItem item: holidayItemList) {
    			if (string.equals(item.getIsHoliday(), "Y")) {
    				cmmtHldy = new CmmtRestde();
    				cmmtHldy.setYmd(item.getLocdate());
    				cmmtHldy.setYmdNm(item.getDateName());
    				cmmtHldy.setUserDsgn(false);
    				holidayList.add(cmmtHldy);
    			}
    		}

    		// 휴일 삭제
    		String yyyymm = year + month;
    		cmmtHldyDao.deleteNotUserDsgn(yyyymm);

    		// 휴일 저장
    		if (holidayList.size() > 0) {
    			cmmtHldyDao.insertList(holidayList);
    			totalItems += holidayList.size();
    		}
    	}

    	return totalItems;
    }
}
