package aicluster.mvn.common.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.security.Code.validateMessage;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dto.MvnFcOfficeRoomDto;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Component
public class MvnUtils
{
    @Autowired
    private UsptMvnFcltyInfoDao usptMvnFcDao;
    @Autowired
    private FwCodeDao codeDao;

	@Autowired
	private UsptResrceInvntryInfoDao uspAlrsrcFninfDao;

	private List<invtVsReqDstb> invtList;

	@Data
	@Builder
	public static class invtVsReqDstb {
		private String rsrcId;
		private String rsrcGroupCd;   /** 자원그룹코드 */
		private String rsrcGroupNm;   /** 자원그룹명 : CMMT_CODE.CODE_NM */
		private String rsrcTypeNm;    /** 자원유형명 */
		private int invtQy;           /** 재고수량 */
		private int dstbQy;           /** 현재배분수량 */
		private int totalQy;          /** 전체수량 */
		private int reqDstbQy;        /** 신청배분수량 */
	}

	/**
	 * 자원재고정보를 기반으로 재고대비신청배분 VO 생성
	 */
	private void setInvtList()
	{
		if (invtList != null && !this.invtList.isEmpty() ) {
			return;
		}

		this.invtList = new ArrayList<>();

		// 자원재고정보 조회
		List<UsptResrceInvntryInfo> fninfList = uspAlrsrcFninfDao.selectList();
		for (UsptResrceInvntryInfo fninf : fninfList) {
			invtList.add(invtVsReqDstb.builder()
					.rsrcId(fninf.getRsrcId())
					.rsrcGroupCd(fninf.getRsrcGroupCd())
					.rsrcGroupNm(fninf.getRsrcGroupNm())
					.rsrcTypeNm(fninf.getRsrcTypeNm())
					.invtQy(fninf.getInvtQy())
					.dstbQy(fninf.getDstbQy())
					.totalQy(fninf.getTotalQy())
					.reqDstbQy(0)
					.build());
		}
	}

    /**
     * 건물동호수 검증 Exception 처리
     *
     * @param bnoRoomNo : 건물동호수 코드
     */
    public void validateBnoRoomNo(String bnoRoomNo)
    {
        if (string.isBlank(bnoRoomNo)) {
            return;
        }

        List<MvnFcOfficeRoomDto> bnoRoomList = usptMvnFcDao.selectBnoRoomCodeList();

        boolean isValid = false;
        for (MvnFcOfficeRoomDto officeRoom : bnoRoomList) {
            if (string.equals(officeRoom.getBnoRoomNo(), bnoRoomNo)) {
                isValid = true;
            }
        }

        if (!isValid) {
            throw new InvalidationException(String.format(validateMessage.유효오류, "입주호실"));
        }
    }

    /**
     * 유효 코드 검증 Exception 처리
     *
     * @param codeGroup : 코드그룹
     * @param code : 코드
     */
    public void validateCode(String codeGroup, String code)
    {
        validateCode(codeGroup, code, null);
    }

    /**
     * 유효 코드 검증 Exception 처리
     *
     * @param codeGroup : 코드그룹
     * @param code : 코드
     * @param codeGroupNm : 코드그룹명
     */
    public void validateCode(String codeGroup, String code, String codeGroupNm)
    {
        if (string.isBlank(code)) {
            return;
        }

        if (string.isBlank(codeGroupNm)) {
            codeGroupNm = "상태코드";
        }

        if (!isValideCode(codeGroup, code)) {
            throw new InvalidationException(String.format(validateMessage.유효오류, codeGroupNm));
        }
    }

    /**
     * 유효 코드 검증
     *
     * @param codeGroup : 코드그룹
     * @param code : 코드
     * @return true/false
     */
    public boolean isValideCode(String codeGroup, String code)
    {
        if (string.isBlank(codeGroup) || string.isBlank(code)) {
            return false;
        }

        List<CodeDto> codeList = codeDao.selectList_enabled(codeGroup);

        boolean isValid = false;
        for (CodeDto codeDto : codeList) {
            if (string.equals(codeDto.getCode(), code)) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * 자원재고 대비 신청 목록 가져오기
     *
     * @return 자원재고대비신청 목록
     */
    public List<invtVsReqDstb> getInvtList()
    {
    	return this.invtList;
    }

    /**
     * 자원재고 대비 신청 목록을 초기화한다.
     */
    public void clearInvtList()
    {
    	if (this.invtList != null) {
	    	this.invtList.clear();
    	}
    }

	/**
	 * 자원ID 존재여부
	 *
	 * @param rsrcId : 자원ID
	 * @return true:있음 / false:없음
	 */
	public boolean isInvtRsrcId (String rsrcId)
	{
		setInvtList();

		return this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().isPresent();
	}

	/**
	 * 자원ID에 대한 누적 할당신청수량이 재고수량 초과여부
	 *
	 * @param rsrcId : 자원ID
	 * @param reqDstbQy : 할당신청수량
	 * @return true:초과 / false:초과안함
	 */
	public boolean isInvtQyExcessYnForAccmltQy(String rsrcId, int reqDstbQy)
	{
		setInvtList();

		invtVsReqDstb invtInf = this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get();

		// 재고수량이 0건인데 신청배분수량이 존재하면 초과 처리
		if ( invtInf.getInvtQy() == 0 && 0 < reqDstbQy) {
			return true;
		}

		// 누적신청배분수량 = 신청배분수량 + 신청배분수량
		int accmltReqDstbQy = invtInf.getReqDstbQy() + reqDstbQy;
		if (invtInf.getInvtQy() < accmltReqDstbQy) {
			return true;
		}
		else {
			invtInf.setReqDstbQy(accmltReqDstbQy);
		}

		return false;
	}

	/**
	 * 자원ID에 대한 재할당 처리 시 할당신청수량에 대한 재고수량 초과여부
	 * (현재 할당수량 + 재고수량과 신청수량을 비교한다.)
	 *
	 * @param rsrcId :
	 * @param dstbQy
	 * @param redstbQy
	 * @return
	 */
	public boolean isInvtQyExcessYnForRedstbQy(String rsrcId, int dstbQy, int redstbQy)
	{
		setInvtList();

		invtVsReqDstb invtInf = this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get();

		// 재고수량이 0건인데 재할당수량이 현재 배분수량보다 많으면 초과 처리
		if ( invtInf.getInvtQy() == 0 && dstbQy < redstbQy) {
			return true;
		}

		// 누적신청배분수량 = 신청배분수량 + (현재 배분수량 - 재배분수량)
		int accmltReqDstbQy = invtInf.getReqDstbQy() + redstbQy;
		if ( (invtInf.getInvtQy() + dstbQy) < accmltReqDstbQy) {
			return true;
		}
		else {
			invtInf.setInvtQy(invtInf.getInvtQy() + dstbQy);
			invtInf.setDstbQy(invtInf.getDstbQy() - dstbQy);
			invtInf.setReqDstbQy(accmltReqDstbQy);
		}

		return false;
	}

	/**
	 * 자원ID에 대한 할당배분수량 재고 반환
	 *
	 * @param rsrcId : 자원ID
	 * @param dstbQy : 배분수량
	 */
	public void setInvtQyReturnDstbQy(String rsrcId, int dstbQy)
	{
		setInvtList();

		invtVsReqDstb invtInf = this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get();

		invtInf.setInvtQy(invtInf.getInvtQy() + dstbQy);
	}

	/**
	 * 자원ID에 대한 자원그룹코드 가져오기
	 *
	 * @param rsrcId : 자원ID
	 * @return 자원그룹코드
	 */
	public String getRsrcGroupCd(String rsrcId)
	{
		setInvtList();

		return this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get().getRsrcGroupCd();
	}

	/**
	 * 자원ID에 대한 자원그룹명 가져오기
	 *
	 * @param rsrcId : 자원ID
	 * @return 자원그룹명
	 */
	public String getRsrcGroupNm(String rsrcId)
	{
		setInvtList();

		return this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get().getRsrcGroupNm();
	}

	/**
	 * 자원ID에 대한 자원유형명 가져오기
	 *
	 * @param rsrcId : 자원ID
	 * @return 자원유형명
	 */
	public String getRsrcTypeNm(String rsrcId)
	{
		setInvtList();

		return this.invtList.stream().filter(obj -> obj.getRsrcId().equals(rsrcId)).findFirst().get().getRsrcTypeNm();
	}
}
