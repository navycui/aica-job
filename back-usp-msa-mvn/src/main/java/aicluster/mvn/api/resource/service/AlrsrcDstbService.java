package aicluster.mvn.api.resource.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.resource.dto.AlrsrcDstbInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcRedstbParam;
import aicluster.mvn.common.dao.UsptResrceAsgnEntrpsDao;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbDao;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbHistDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;
import aicluster.mvn.common.entity.UsptResrceAsgnDstb;
import aicluster.mvn.common.entity.UsptResrceAsgnDstbHist;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import aicluster.mvn.common.util.MvnUtils.invtVsReqDstb;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlrsrcDstbService {

    @Autowired
	private UsptResrceAsgnEntrpsDao cmpnyDao;

    @Autowired
    private UsptResrceAsgnDstbDao dstbDao;

    @Autowired
    private UsptResrceAsgnDstbHistDao histDao;

    @Autowired
    private UsptResrceInvntryInfoDao fninfDao;

    @Autowired
    private FwCodeDao codeDao;

    @Autowired
    private MvnUtils mvnUtils;

    public void modify(AlrsrcRedstbParam param)
    {
        Date now = new Date();
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        UsptResrceAsgnEntrps cmpnyInf = cmpnyDao.select(param.getAlrsrcId());
        if (cmpnyInf == null) {
            throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
        }

        if (string.equals(cmpnyInf.getAlrsrcSt(), CodeExt.alrsrcSt.이용종료)) {
            throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "이용대기이거나 이용중인", "재할당"));
        }

        // 자원재고 대비 신청수량 목록 초기화
        mvnUtils.clearInvtList();

        // 입력값의 자원그룹코드 목록
        List<String> inRsrcGroupCdList = new ArrayList<>();

        // 입력값 내에 자원ID 중복 검증(입력 parameter에서 동일한 rsrcId 값을 필터링하여 건수가 1건 초과한 경우 중복 건 있음으로 판정)
        for (AlrsrcDstbInsListItem insParam : param.getAlrsrcDstbInsList()) {
            // 중복 자원ID 검증
        	int duplicateCntForRsrcId = param.getAlrsrcDstbInsList().stream().filter(obj->obj.getRsrcId().equals(insParam.getRsrcId())).toArray().length;
            if (duplicateCntForRsrcId > 1) {
                UsptResrceInvntryInfo rsrcFninf = fninfDao.select(insParam.getRsrcId());
                throw new InvalidationException(String.format("자원할당 목록에 [%s]의 [%s] 할당 정보가 중복됩니다.[%s]", rsrcFninf.getRsrcGroupNm(), rsrcFninf.getRsrcTypeNm(), insParam.getRsrcId()));
            }

            // 할당 자원ID에 대한 자원그룹코드 목록 변수 등록
            inRsrcGroupCdList.add(mvnUtils.getRsrcGroupCd(insParam.getRsrcId()));
        }

        // 입력값 내에 자원그룹코드가 모두 있는지 검증(가속기(GPU), 스토리지(STORAGE), SaaS(SAAS), 데이터레이크(DATALAKE))
        List<CodeDto> codeList = codeDao.selectList_enabled("RSRC_GROUP");
        for (CodeDto code : codeList) {
        	if (!array.contains(inRsrcGroupCdList.toArray(), code.getCode())) {
        		throw new InvalidationException(String.format("자원할당 목록에 [%s]에 해당하는 할당 정보가 없습니다.", code.getCodeNm()));
        	}
        }

        // 현재 할당되어 있는 배분정보 조회
        List<UsptResrceAsgnDstb> dstbList = dstbDao.selectList(param.getAlrsrcId());
        if (dstbList == null) {
            throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 대한 자원할당 배분정보"));
        }

        // 입력 데이터 검증
        InvalidationsException errs = new InvalidationsException();

        // 자원재고정보 검증 및 재고수량 대비 할당신청수량 검증
        for (AlrsrcDstbInsListItem insParam : param.getAlrsrcDstbInsList()) {
            if (!mvnUtils.isInvtRsrcId(insParam.getRsrcId())) {
                log.error(String.format(validateMessageExt.포함불가, "자원ID"));
                errs.add("alrsrcDstbInsList", String.format("자원할당 목록에 " + validateMessageExt.포함불가, "자원ID"));
            }
            else {
                boolean invtExcessYn = false;
                int rsrcReqDstbQy = Long.valueOf(insParam.getRsrcDstbQy()).intValue();
                boolean sameDstbYn = dstbList.stream().filter(obj->obj.getRsrcId().equals(insParam.getRsrcId())).findFirst().isPresent();
                if (sameDstbYn) {
                    UsptResrceAsgnDstb dstb = dstbList.stream().filter(obj->obj.getRsrcId().equals(insParam.getRsrcId())).findFirst().get();
                    invtExcessYn = mvnUtils.isInvtQyExcessYnForRedstbQy(insParam.getRsrcId(), dstb.getRsrcDstbQy(), rsrcReqDstbQy);
                }
                else {
                    invtExcessYn = mvnUtils.isInvtQyExcessYnForAccmltQy(insParam.getRsrcId(), rsrcReqDstbQy);
                }

                if (invtExcessYn) {
                    String rsrcGroupNm = mvnUtils.getRsrcGroupNm(insParam.getRsrcId());
                    String rsrcTypeNm = mvnUtils.getRsrcTypeNm(insParam.getRsrcId());

                    log.error(String.format("[%s]의 [%s] 자원유형의 자원할당 신청수량이 재고수량를 초과하였습니다.", rsrcGroupNm, rsrcTypeNm));
                    errs.add("alrsrcDstbInsList", String.format("[%s]의 [%s] 자원유형의 자원할당 신청수량이 재고수량를 초과하였습니다.", rsrcGroupNm, rsrcTypeNm));
                }
            }
        }

        if (errs.size() > 0) {
            throw errs;
        }

        // 삭제할 자원할당배분정보를 자원재고로 반환 처리
        for (UsptResrceAsgnDstb dstbInf : dstbList) {
            boolean delTrgetYn = param.getAlrsrcDstbInsList().stream().filter(obj->obj.getRsrcId().equals(dstbInf.getRsrcId())).findFirst().isPresent() == true ? false : true;
            if (delTrgetYn) {
                mvnUtils.setInvtQyReturnDstbQy(dstbInf.getRsrcId(), dstbInf.getRsrcDstbQy());
            }
        }

        // 이력데이터 생성 및 재고수량 반환 데이터 생성
        List<UsptResrceAsgnDstbHist> dstbHist = new ArrayList<>();
        for (UsptResrceAsgnDstb dstbInf : dstbList) {
            UsptResrceAsgnDstbHist hist = UsptResrceAsgnDstbHist.builder()
                                            .histId(string.getNewId(prefixId.이력ID))
                                            .histDt(now)
                                            .firstDstbYn(false)
                                            .workTypeNm("자원재할당")
                                            .workerId(worker.getMemberId())
                                            .build();

            property.copyProperties(hist, dstbInf);

            dstbHist.add(hist);
        }

        // 재할당 데이터 생성
        List<UsptResrceAsgnDstb> insDstbList = new ArrayList<UsptResrceAsgnDstb>();
        for (AlrsrcDstbInsListItem insParam : param.getAlrsrcDstbInsList()) {
            UsptResrceAsgnDstb insDstb = UsptResrceAsgnDstb.builder()
                                            .alrsrcId(param.getAlrsrcId())
                                            .creatorId(worker.getMemberId())
                                            .createdDt(now)
                                            .updaterId(worker.getMemberId())
                                            .updatedDt(now)
                                            .build();

            insDstb.setRsrcId(insParam.getRsrcId());
            insDstb.setRsrcUseYn(insParam.getRsrcUseYn());
            insDstb.setRsrcDstbQy(Long.valueOf(insParam.getRsrcDstbQy()).intValue());
            insDstb.setRsrcDstbCn(insParam.getRsrcDstbCn());

            insDstbList.add(insDstb);
        }

        // 현재 자원배분정보 전체 delete
        dstbDao.delete(param.getAlrsrcId());

        // 재할당 자원배분정보 insert
        dstbDao.insertList(insDstbList);

        // 자원배분정보이력 insert
        histDao.insertList(dstbHist);

        // 재고 수량 update
        log.info(mvnUtils.getInvtList().toString());
        for (invtVsReqDstb invtInf : mvnUtils.getInvtList()) {
            int invtQy = invtInf.getInvtQy() - invtInf.getReqDstbQy();
            fninfDao.updateInvtQy(invtInf.getRsrcId(), invtQy, invtInf.getTotalQy(), worker.getMemberId());
        }
    }

}
