package aicluster.pms.api.evl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.pms.common.dao.UsptExpertDao;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertParam;
import aicluster.pms.common.util.Code;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvlExpertService {

	public static final long ITEMS_PER_PAGE = 10L;

	@Autowired
	private UsptExpertDao usptExpertDao;

	//발표자료 제출대상 목록조회
	public CorePagination<UsptExpert> getExpertList(UsptExpertParam usptExpert, Long page) {

		if(page == null) {
			page = 1L;
		}

		if(usptExpert.getItemsPerPage() == null) {
			usptExpert.setItemsPerPage(ITEMS_PER_PAGE);
		}

		long totalItems = usptExpertDao.selectListCount(usptExpert);

		usptExpert.setExcel(false);

		//조회할 페이지의 구간 정보
		CorePaginationInfo info = new CorePaginationInfo(page, usptExpert.getItemsPerPage(), totalItems);

		usptExpert.setBeginRowNum(info.getBeginRowNum());

		List<UsptExpert> list = usptExpertDao.selectList(usptExpert);
		log.debug("email = "+list.get(0).getEmail());
		log.debug("birthdy = "+list.get(0).getBrthdy());
		log.debug("mbtlnum = "+list.get(0).getMbtlnum());

		//출력할 자료 생성 후 리턴
		CorePagination<UsptExpert> pagination = new CorePagination<>(info, list);

		return pagination;
	}


	//전문가 목록 엑셀다운로드
	public List<UsptExpert> getExpertListExcel(UsptExpertParam usptExpert) {
		usptExpert.setExcel(true);
		return usptExpertDao.selectList(usptExpert);
	}


	//전문가 등록
	public List<UsptExpert> add(List<UsptExpert> usptExpertList){
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		InvalidationsException inputValidateErrs = new InvalidationsException();

		for(UsptExpert regInfo : usptExpertList) {

			if(CoreUtils.string.isEmpty(regInfo.getExpertNm())) {
				inputValidateErrs.add("expertNm",String.format(Code.validateMessage.입력없음, "전문가명"));
			}

			if(CoreUtils.string.isEmpty(regInfo.getGenderCd())) {
				inputValidateErrs.add("genderCd", String.format(Code.validateMessage.입력없음, "성별코드"));
			}

			if(regInfo.getNativeYn() == null) {
				inputValidateErrs.add("nativeYn", String.format(Code.validateMessage.입력없음, "내국인여부"));
			}

			if(CoreUtils.string.isEmpty(regInfo.getEncBrthdy())) {
				inputValidateErrs.add("encBrthdy", String.format(Code.validateMessage.입력없음, "생년월일"));
			}

			if(CoreUtils.string.isEmpty(regInfo.getEncMbtlnum())) {
				inputValidateErrs.add("encMbtlnum",String.format(Code.validateMessage.입력없음, "휴대폰번호"));
			}

			if(CoreUtils.string.isEmpty(regInfo.getEncEmail())) {
				inputValidateErrs.add("encEmail",String.format(Code.validateMessage.입력없음, "이메일"));
			}

			if(CoreUtils.string.isEmpty(regInfo.getWrcNm())) {
				inputValidateErrs.add("wrcNm",String.format(Code.validateMessage.입력없음, "직장명"));
			}
		}

		if (inputValidateErrs.size() > 0) {
			throw new InvalidationException(inputValidateErrs.getExceptionMessages().stream().map(x->x.getMessage()).collect(Collectors.joining("\n")));
		}

		String _flag;

		List<UsptExpert> insertlistInfo = new ArrayList<>();

		for(UsptExpert regInfo : usptExpertList) {
			_flag = regInfo.getFlag();

			if (string.equals(_flag, "I") ) {

				//기존 등록 여부 체크
				UsptExpertParam paramInfo = new UsptExpertParam();

//				paramInfo.setEncBrthdy(regInfo.getEncBrthdy());
//				paramInfo.setEncMbtlnum(regInfo.getEncMbtlnum());
//				paramInfo.setEncEmail(regInfo.getEncEmail());

				//전체 조회하여 생년월일, 휴대폰 번호, 이메일 모두 일치하면 중복건으로 판단
				List<UsptExpert> resultList = usptExpertDao.selectList(paramInfo);

				for(UsptExpert resultInfo : resultList) {
					if( string.equals(resultInfo.getEmail(), regInfo.getEncEmail())){
						throw new InvalidationException(regInfo.getEncEmail() + " 이메일은 이미 등록된 이메일 주소입니다.(이메일 중복 오류)");
					}
				}

				for(UsptExpert resultInfo : resultList) {
					if( string.equals(resultInfo.getEmail(), regInfo.getEncEmail())
							&& string.equals(resultInfo.getMbtlnum(), regInfo.getEncMbtlnum())
							&& string.equals(resultInfo.getBrthdy(), regInfo.getEncBrthdy()) ){
						throw new InvalidationException(regInfo.getExpertNm() + "전문가는 중복된 전문가입니다.(생년월일, 휴대폰번호, 이메일");
					}
				}

				Date now = new Date();

				String insertKey = string.getNewId("evlexpert-");
				regInfo.setExpertId(insertKey);
				regInfo.setCreatedDt(now);
				regInfo.setCreatorId(worker.getMemberId());

				String encEmail = CoreUtils.aes256.encrypt(regInfo.getEncEmail(), insertKey);
				String encBrthdy = CoreUtils.aes256.encrypt(regInfo.getEncBrthdy(), insertKey);
				String encMbtlnum = CoreUtils.aes256.encrypt(regInfo.getEncMbtlnum(), insertKey);

				regInfo.setEncEmail(encEmail);
				regInfo.setEncBrthdy(encBrthdy);
				regInfo.setEncMbtlnum(encMbtlnum);

				insertlistInfo.add(regInfo);
			}

		}

		int insertCnt = usptExpertDao.insertList(insertlistInfo);

		if (insertCnt < 0) {
			throw new LoggableException(String.format(Code.validateMessage.DB오류, "저장"));
		}


		return usptExpertList;
	}

}
