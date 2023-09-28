package aicluster.tsp.api.admin.eqpmn.category.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.tsp.api.admin.eqpmn.category.param.EquipmentCategoryParam;
import aicluster.tsp.common.dao.TsptEqpmnClDao;
import aicluster.tsp.common.entity.TsptEqpmnCl;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentCategoryService {

	@Autowired
	private TsptEqpmnClDao tsptEqpmnClDao;

	/**
	 * 조회 : 클릭 후 나오는 카테고리 리스트
	 * */
	public List<TsptEqpmnCl> selectList(String eqpmnClId) {
		return tsptEqpmnClDao.selectCatrgoryList(eqpmnClId);
	}

	/**
	 * 조회 : 모든 카테고리의 리스트
	 * */
	public List<TsptEqpmnCl> selectAllList() {
		return tsptEqpmnClDao.selectCatrgoryAllList();
	}

	/**
	 * 저장 : 저장 버튼 클릭해서 저장
	 */
	public List<TsptEqpmnCl> save(List<EquipmentCategoryParam> param, String _eqpmnClId) {

		if (param != null && param.size() > 0) {
			List<TsptEqpmnCl> list = new ArrayList<>();
			TsptEqpmnCl tsptEqpmnCl = null;
			Integer index = 0;
			String eqpmnLclasId = "";

			// 유효성 체크는 front, backend 모두가 하지만 특히 백엔드에서는 무조건 해줘야 한다.

			// for문 전에 현재 Level을 체크해줘서 Depth를 추가 가능한지 확인
			if(!_eqpmnClId.equals("ROOT") && tsptEqpmnClDao.selectCategoryLevel(_eqpmnClId) > 2 ){
				throw new InvalidationException("더이상 Depth를 추가할 수 없습니다.");
			}
			BnMember member = TspUtils.getMember();
			// 가저욘 param의 데이터로 for문 돌리기
			for (EquipmentCategoryParam equipmentCategoryParam : param) {
				if(CoreUtils.string.isBlank(equipmentCategoryParam.getEqpmnClNm())){
					throw new InvalidationException("분류명에 공백이 있습니다.");
				} else {
					String check = tsptEqpmnClDao.selectCategoryCheck(equipmentCategoryParam.getEqpmnClNm(),_eqpmnClId);
					if(CoreUtils.string.isNotBlank(check) && CoreUtils.string.isBlank(equipmentCategoryParam.getEqpmnClId())){
						throw new InvalidationException("이름이 중복되었습니다.");
					}
				}

				// if(equipmentCategoryParam.getEqpmnClfcId().length() == 0) 이러한 경우는 시큐어 코딩 프로그램에서 보안취약점 바로 걸려버리기 때문에 isEmpty 나 isBlank 를 사용 하는데
				// 특별한 경우가 아니라면 isBlank 를 주로 사용하는 방향으로 진행
				// isEmpty 는 null, "" 일 경우, isBlank는 null, "", " " 일 경우

				// 신규 ID일 경우 받은 데이터에 Id 가 없기 때문에 없으면 새로운 Id를 입력 아닐경우 온 그대로 사용
				if(CoreUtils.string.isBlank(equipmentCategoryParam.getEqpmnClId())){
					eqpmnLclasId = CoreUtils.string.getNewId("eqpmnClfc-");
				} else {
					eqpmnLclasId = equipmentCategoryParam.getEqpmnClId();
				}

				// Insert, Update 시에는 entity에 데이터를 셋팅 후에 넣는 방식으로 진행
				// entity에 데이터 셋팅
				tsptEqpmnCl = TsptEqpmnCl.builder()
						.eqpmnClId( eqpmnLclasId)
						.eqpmnClNm(equipmentCategoryParam.getEqpmnClNm())
						.eqpmnLclasId(equipmentCategoryParam.getEqpmnLclasId())
						.ordr(++index)
						.useAt(equipmentCategoryParam.isUseAt())
						.creatrId(member.getMemberId())
						.build();

				list.add(tsptEqpmnCl);

			}
			if (list.size() > 0) {
				// 데이터 save
				// upsert 를 사용해서 추가 하고 에러 발생시 업데이트를 진행 하도록 쿼리문을 작성 하였음
				tsptEqpmnClDao.insertCategory(list);
			}
		}

		// 저장 후에 최신 데이터를 조회해서 반환
		return tsptEqpmnClDao.selectCatrgoryList(_eqpmnClId);
	}

	/**
	 * 삭제 : 삭제 버튼 클릭으로 데이터 삭제
	 */
	public List<TsptEqpmnCl> delete(List<EquipmentCategoryParam> param) {
		String eqpmnLclasId = "";
		// 데이터 삭제
		for(EquipmentCategoryParam equipmentCategoryParam : param) {
			if(CoreUtils.string.isBlank(eqpmnLclasId))
                eqpmnLclasId = equipmentCategoryParam.getEqpmnLclasId()
                        ;

			// tspt_eqpmn 테이블에 있는 장비들이 삭제 하려는 카테고리 및 하위 카테도리에 물려 있으면 ERROR 를 반환 한다.
			if(tsptEqpmnClDao.selectEqpmnCount(equipmentCategoryParam.getEqpmnClId()) != 0) {
				throw new InvalidationException("하위 카테고리가 존재합니다.");
			}

			if(tsptEqpmnClDao.selectEqpmnClIdCount(equipmentCategoryParam.getEqpmnClId()) > 0){
				throw new InvalidationException("카테고리내에 등록된 장비가 있습니다.");
			}
			// 삭제 진행
			if(tsptEqpmnClDao.deleteCategory(equipmentCategoryParam.getEqpmnClId())< 1) {
				// 삭제된 데이터가 없을경우 에러 출력
				throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "삭제"));
			}
		}

		tsptEqpmnClDao.updateSoreOrder(eqpmnLclasId);
		return tsptEqpmnClDao.selectCatrgoryList(eqpmnLclasId);
	}
}

