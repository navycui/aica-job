package aicluster.member.api.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.auth.dto.MenuOrderRequestDto;
import aicluster.member.api.auth.dto.MyMenuDto;
import aicluster.member.common.dao.CmmtCodeDao;
import aicluster.member.common.dao.CmmtMenuDao;
import aicluster.member.common.dao.CmmtMenuRoleDao;
import aicluster.member.common.dao.CmmvUserDao;
import aicluster.member.common.entity.CmmtCode;
import aicluster.member.common.entity.CmmtMenu;
import aicluster.member.common.entity.CmmtMenuRole;
import aicluster.member.common.entity.CmmvUser;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.prefix;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

@Service
public class MenuService {

	@Autowired
	private CmmtMenuDao cmmtMenuDao;

	@Autowired
	private CmmtMenuRoleDao cmmtMenuRoleDao;

	@Autowired
	private CmmtCodeDao cmmtCodeDao;

	@Autowired
	private CmmvUserDao cmmvUserDao;

	/**
	 * 메뉴등록 시 사용되는 relation 변수에 허용할 고정값 목록
	 */
	private List<String> relations = Arrays.asList(CodeExt.menuRelation.자식
									            , CodeExt.menuRelation.위
									            , CodeExt.menuRelation.아래
									            );

	/**
	 * 메뉴이동 시 사용되는 direction 변수에 허용할 고정값 목록
	 */
	private List<String> directions = Arrays.asList(CodeExt.menuDirection.위
									            , CodeExt.menuDirection.아래
									            );

	/**
	 * 시스템ID 값에 대한 검증
	 *
	 * @param systemId	시스템ID
	 */
	private void validateSystemId (String systemId)
	{
		// 시스템ID 유무 검증
		CmmtCode cmmtSystemCode = cmmtCodeDao.select("SYSTEM_ID", systemId);
		if (cmmtSystemCode == null) {
			throw new InvalidationException(String.format(validateMessageExt.입력오류, "시스템ID"));  // 잘못된 %s 입니다. %s를 다시 확인하세요.
		}
	}

	/**
	 * 시스템ID, 메뉴ID를 검증하고 메뉴ID에 해당하는 메뉴정보를 조회하여 리턴한다.
	 *
	 * @param systemId	시스템ID
	 * @param menuId	메뉴ID
	 */
	private CmmtMenu getMenuDtoAftValid (String systemId, String menuId)
	{
		// 시스템ID 검증
		validateSystemId(systemId);

		// 입력값 Blank 검증
		if (string.isBlank(menuId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "메뉴ID"));  // %s를 입력하세요.
		}

		// 메뉴ID prefix 검증
		if (!string.startsWith(menuId, CodeExt.prefix.MENU) && !string.equals(menuId, CodeExt.menuId.ROOT)) {
			throw new InvalidationException(String.format(validateMessageExt.접두어오류, "메뉴ID", prefix.MENU, menuId));  // %s는 '%s'로 시작되어야 합니다.[%s]
		}

		// 메뉴정보 유무 검증
		CmmtMenu cmmtMenu = cmmtMenuDao.select(systemId, menuId);
		if (cmmtMenu == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "메뉴 정보"));  // %s이(가) 없습니다.
		}

		return cmmtMenu;
	}

	/**
	 * 메뉴 전체 목록 조회
	 *
	 * @param systemId	시스템ID
	 */
	public JsonList<CmmtMenu> getList(String systemId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 시스템ID 검증
		validateSystemId(systemId);

		// 메뉴 목록 조회
		List<CmmtMenu> list = cmmtMenuDao.selectAll(systemId);

		// 메뉴가 없는 경우 Root 메뉴 생성 후 재조회
		if (list.size() == 0) {
			CmmtMenu rootMenu = CmmtMenu.builder()
										.systemId(systemId)
										.menuId(CodeExt.menuId.ROOT)
										.menuNm(CodeExt.menuId.ROOT)
										.url("/")
										.parentMenuId(null)
										.sortOrder((long)0)
										.build();
			// Root 메뉴 정보 입력
			cmmtMenuDao.insert(rootMenu);

			// 메뉴 목록 재조회
			list = cmmtMenuDao.selectAll(systemId);
		}

		return new JsonList<>(list);
	}

	/**
	 * 메뉴정보 등록
	 *  - 기준메뉴ID를 기준으로 자식 메뉴의 최하단 또는 동레벨의 위/아래로 메뉴를 등록한다.
	 *
	 * @param systemId		시스템ID
	 * @param baseMenuId	기준 메뉴ID
	 * @param relation		기준 메뉴와의 연관관계(자식(CHILD)/동레벨위(ABOVE)/동레벨아래(BELOW))
	 * @param menuNm		메뉴명칭
	 * @param url			메뉴 URL
	 * @param newWindow		새창여부(true/false)
	 * @param remark		비고
	 */
	public CmmtMenu insertMenu(String systemId, String baseMenuId, String relation, String menuId, String menuNm, String url,
			Boolean newWindow, String remark)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		String newMenuId = menuId;
		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 시스템ID 검증
		validateSystemId(systemId);

		// 입력값 검증
		if (string.isBlank(baseMenuId)) {
			inputValidateErrs.add("baseMenuId", String.format(validateMessageExt.입력없음, "기준 메뉴ID"));  //%s을(를) 입력하세요.
		}
		else {
			// 기준메뉴ID에 대한 prefix 및 ROOT ID 검증
			if (!string.startsWith(baseMenuId, CodeExt.prefix.MENU) && !string.equals(baseMenuId, CodeExt.menuId.ROOT)) {
				inputValidateErrs.add("baseMenuId", String.format(validateMessageExt.접두어오류, "기준메뉴ID", prefix.MENU, baseMenuId));  // %s은(는) '%s'로 시작되어야 합니다.[%s]
			}
		}
		if (string.isBlank(menuId)) {
			inputValidateErrs.add("menuId", String.format(validateMessageExt.입력없음, "신규 메뉴ID"));  //%s을(를) 입력하세요.
		}
		if (string.isBlank(relation)) {
			inputValidateErrs.add("relation", String.format(validateMessageExt.입력없음, "relation"));  //%s을(를) 입력하세요.
		}
		else {
			// 입력값 코드 검증
			if (!relations.contains(relation)) {
				inputValidateErrs.add("relation", String.format(validateMessageExt.허용불가, "relation", relation));  // %s이(가) 허용된 값이 아닙니다.[%s]
			}
		}
		if (string.isBlank(menuNm)) {
			inputValidateErrs.add("menuNm", String.format(validateMessageExt.입력없음, "메뉴명"));  //%s을(를) 입력하세요.
		}
		if (string.isBlank(url)) {
			inputValidateErrs.add("url", String.format(validateMessageExt.입력없음, "메뉴 URL"));  //%s을(를) 입력하세요.
		}
		if (newWindow == null) {
			inputValidateErrs.add("newWindow", String.format(validateMessageExt.입력없음, "메뉴 새창여부"));  //%s을(를) 입력하세요.
		}

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// Root 메뉴인 경우 relation 값으로 'CHILD'만 허용
		if (string.equals(baseMenuId, CodeExt.menuId.ROOT)) {
			if (!string.equals(relation, CodeExt.menuRelation.자식)) {
				throw new InvalidationException("해당 메뉴와 같은 레벨로는 추가할 수 없습니다.");
			}
		}

		// 신규메뉴ID prefix 연결
		if (!string.startsWith(newMenuId, CodeExt.prefix.MENU)) {
			newMenuId = CodeExt.prefix.MENU + newMenuId;
		}

		// 기준메뉴ID 정보 유무 검증
		CmmtMenu baseMenu = cmmtMenuDao.select(systemId, baseMenuId);
		if (baseMenu == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "기준 메뉴"));  // %s이(가) 없습니다.
		}

		// 신규메뉴ID 정보 유무 검증
		CmmtMenu validMenu = cmmtMenuDao.select(systemId, newMenuId);
		if (validMenu != null) {
			throw new InvalidationException(String.format(validateMessageExt.중복오류, "메뉴ID", menuId));  // 사용 중인 %s 입니다.[%s]
		}

		// 메뉴정보 Entity 생성
		CmmtMenu cmmtMenu = CmmtMenu.builder()
									.systemId(systemId)
									.menuId(newMenuId)
									.menuNm(menuNm)
									.url(url)
									.newWindow(newWindow)
									.parentMenuId(baseMenu.getMenuId())
									.remark(remark)
									.build();

		// 테이블 공통 컬럼 세팅
		cmmtMenu.setCreatedDt(now);
		cmmtMenu.setCreatorId(worker.getMemberId());
		cmmtMenu.setUpdatedDt(now);
		cmmtMenu.setUpdaterId(worker.getMemberId());

		// 기준메뉴의 자식 레벨로 마지막 위치되도록 정렬순번 처리
		if (string.equals(relation, CodeExt.menuRelation.자식)) {
			// 기준메뉴의 자식 메뉴정보를 기반으로 기준 정렬순번 정의
			CmmtMenu maxHierarchyMenu = cmmtMenuDao.selectLastChild(systemId, baseMenuId);
			if (maxHierarchyMenu == null) {
				cmmtMenu.setSortOrder(0L);
			}
			else {
				cmmtMenu.setSortOrder(maxHierarchyMenu.getSortOrder());
			}

			// 정렬순번 증가
			cmmtMenu.setSortOrder(cmmtMenu.getSortOrder() + 1);
		}
		// 기준메뉴와 동일 레벨로 하여 한단계 위에 위치하도록 정렬순번 처리
		else if (string.equals(relation, CodeExt.menuRelation.위)) {
			// 정렬순번 정의(기준메뉴 정렬순번으로 정의)
			cmmtMenu.setSortOrder(baseMenu.getSortOrder());
		}
		// 기준메뉴와 동일 레벨로 하여 한단계 아래에 위치하도록 정렬순번 처리
		else if (string.equals(relation, CodeExt.menuRelation.아래)) {
			// 정렬순번 정의(기준메뉴 보다 1 증가로 정렬순번 정의)
			cmmtMenu.setSortOrder(baseMenu.getSortOrder() + 1);
		}

		// 동일 레벨 메뉴 삽입인 경우 후위 메뉴에 대한 정렬순번 및 부모메뉴ID 처리
		if (!string.equals(relation, CodeExt.menuRelation.자식)) {
			// 기준메뉴 부모메뉴ID 세팅
			cmmtMenu.setParentMenuId(baseMenu.getParentMenuId());

			// 메뉴 정렬순번 재설정(입력 메뉴의 정렬 순번보다 크거나 같은 메뉴들을 모두 1씩 증가)
			cmmtMenuDao.incSortOrder(systemId, baseMenu.getMenuId(), cmmtMenu.getSortOrder());
		}

		// 메뉴정보 입력
		cmmtMenuDao.insert(cmmtMenu);

		// 입력된 메뉴정보 조회
		return cmmtMenuDao.select(cmmtMenu.getSystemId(), cmmtMenu.getMenuId());
	}

	/**
	 * 메뉴정보 조회
	 *
	 * @param systemId	시스템ID
	 * @param menuId	메뉴ID
	 */
	public CmmtMenu selectMenu(String systemId, String menuId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 시스템ID, 메뉴ID 검증 후 메뉴정보 DTO 가져오기
		CmmtMenu cmmtMenu = getMenuDtoAftValid(systemId, menuId);

		return cmmtMenu;
	}

	/**
	 * 메뉴정보 수정
	 *
	 * @param systemId		시스템ID
	 * @param menuId		메뉴ID
	 * @param menuNm		메뉴명칭
	 * @param url			메뉴 URL
	 * @param newWindow		새창여부(true/false)
	 * @param remark		비고
	 */
	public CmmtMenu updateMenu(String systemId, String menuId, String menuNm, String url, Boolean newWindow,
			String remark)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		InvalidationsException inputValidateErrs = new InvalidationsException();

		// 시스템ID, 메뉴ID 검증 후 메뉴정보 DTO 가져오기
		CmmtMenu cmmtMenu = getMenuDtoAftValid(systemId, menuId);

		// 입력값 Blank 검증
		if (string.isBlank(menuNm)) {
			inputValidateErrs.add("menuNm", String.format(validateMessageExt.입력없음, "메뉴명"));  // %s을(를) 입력하세요.
		}
		if (string.isBlank(url)) {
			inputValidateErrs.add("url", String.format(validateMessageExt.입력없음, "메뉴 URL"));  // %s을(를) 입력하세요.
		}
		if (newWindow == null) {
			inputValidateErrs.add("newWindow", String.format(validateMessageExt.입력없음, "메뉴 새창여부"));  // %s을(를) 입력하세요.
		}

		// 입력값 검증 결과 메시지 처리
		if (inputValidateErrs.size() > 0) {
			throw inputValidateErrs;
		}

		// 메뉴 변경정보 세팅
		cmmtMenu.setMenuNm(menuNm);
		cmmtMenu.setUrl(url);
		cmmtMenu.setNewWindow(newWindow);
		cmmtMenu.setRemark(remark);

		// 테이블 공통 컬럼 세팅
		cmmtMenu.setUpdatedDt(now);
		cmmtMenu.setUpdaterId(worker.getMemberId());

		// 메뉴정보 수정
		cmmtMenuDao.update(cmmtMenu);

		// 메뉴정보 재조회
		return cmmtMenuDao.select(cmmtMenu.getSystemId(), cmmtMenu.getMenuId());
	}

	/**
	 * 메뉴정보 삭제
	 *  - 단건 삭제이며, 자식메뉴가 존재하거나 역할과 매핑된 경우 삭제 불가
	 *
	 * @param systemId	시스템ID
	 * @param menuId	메뉴ID
	 */
	public void deleteMenu(String systemId, String menuId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 시스템ID, 메뉴ID 검증 후 메뉴정보 DTO 가져오기
		CmmtMenu cmmtMenu = getMenuDtoAftValid(systemId, menuId);

		// 자식 메뉴 존재유무 검증
		List<CmmtMenu> childMenuList = cmmtMenuDao.selectChildAll(systemId, menuId);
		if (childMenuList.size() > 0) {
			throw new InvalidationException(String.format(validateMessageExt.삭제불가, "하위 메뉴 존재"));  // 데이터를 삭제할 수 없습니다.[사유:%s]
		}

		// 메뉴-역할 매핑 검증
		List<CmmtMenuRole> menuRoleList = cmmtMenuRoleDao.selectByMenuId(systemId, menuId);
		if (menuRoleList.size() > 0) {
			throw new InvalidationException(String.format(validateMessageExt.삭제불가, "메뉴-역할 매핑 존재"));  // 데이터를 삭제할 수 없습니다.[사유:%s]
		}

		// 삭제될 메뉴의 정렬순번보다 큰 메뉴들에 대한 정렬순번 1씩 감소 처리
		cmmtMenuDao.decSortOrder(systemId, menuId, cmmtMenu.getSortOrder());

		// 메뉴정보 삭제
		cmmtMenuDao.delete(systemId, menuId);
	}

	/**
	 * 메뉴 정렬 위/아래 이동
	 *  - 메뉴의 위/아래 한 단계씩 이동한다.(자식 메뉴도 포함되어 이동한다)
	 *
	 * @param systemId	시스템ID
	 * @param menuId	메뉴ID
	 * @param direction	이동구분(위(UP)/아래(DOWN))
	 */
	public CmmtMenu moveMenu(String systemId, String menuId, String direction)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		long startSortOrder = -1;
		List<CmmtMenu> moveMenuList = new ArrayList<>();

		// 시스템ID, 메뉴ID 검증 후 메뉴정보 DTO 가져오기
		CmmtMenu cmmtMenu = getMenuDtoAftValid(systemId, menuId);

		// 입력값 Blank 검증
		if (string.isBlank(direction)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "direction"));  // %s을(를) 입력하세요.
		}

		// 입력값 코드 검증
		if (!directions.contains(direction)) {
			throw new InvalidationException(String.format(validateMessageExt.허용불가, "direction", direction));  // %s이(가) 허용된 값이 아닙니다.[%s]
		}

		// 이동 구분별 검증 및 정보 생성
		if (string.equals(direction, CodeExt.menuDirection.위)) {
			// 이동할 메뉴와 동일한 부모를 가진 메뉴 중 바로 위에 위치한 메뉴 정보 조회
			CmmtMenu upMenu = cmmtMenuDao.selectAboveMenu(systemId, cmmtMenu.getMenuId());
			if (upMenu == null) {
				throw new InvalidationException("더 이상 위로 옮길 수 없습니다.");
			}

			// 시작 정렬순번 정의
			startSortOrder = upMenu.getSortOrder();

			// 이동 대상 메뉴 목록 생성
			moveMenuList.add(cmmtMenu);
			moveMenuList.add(upMenu);
		}
		else if (string.equals(direction, CodeExt.menuDirection.아래)) {
			// 이동할 메뉴와 동일한 부모를 가진 메뉴 중 바로 아래에 위치한 메뉴 정보 조회
			CmmtMenu downMenu = cmmtMenuDao.selectBelowMenu(systemId, cmmtMenu.getMenuId());
			if (downMenu == null) {
				throw new InvalidationException("더 이상 아래로 옮길 수 없습니다.");
			}

			// 시작 정렬순번 정의
			startSortOrder = cmmtMenu.getSortOrder();

			// 이동 대상 메뉴 목록 생성
			moveMenuList.add(downMenu);
			moveMenuList.add(cmmtMenu);
		}

		// 정렬 순번 생성 및 메뉴정보 수정
		for (CmmtMenu moveMenu : moveMenuList) {
			moveMenu.setSortOrder(startSortOrder);

			// 테이블 공통 컬럼 세팅
			moveMenu.setUpdatedDt(now);
			moveMenu.setUpdaterId(worker.getMemberId());

			// 변경된 정렬순번 저장
			cmmtMenuDao.update(moveMenu);

			// 정렬순번 증가
			startSortOrder++;
		}

		// 변경된 메뉴정보 재조회
		return cmmtMenuDao.select(systemId, cmmtMenu.getMenuId());
	}

	/**
	 * 사용자 권한에 해당하는 메뉴 목록을 조회한다.
	 * (미로그인 상태인 경우 '미로그인(ROLE_ANONYMOUS)' 역할에 해당하는 메뉴 목록을 조회한다.)
	 *
	 * @param systemId	시스템ID
	 */
	public JsonList<MyMenuDto> getUserMenuList(String systemId)
	{
		BnMember worker = SecurityUtils.getCurrentMember();
		String memberId = null;

		// 회원 ID 추출
		if (worker != null) {
			memberId = worker.getMemberId();
		}

		// 시스템ID 검증
		validateSystemId(systemId);

		// 미로그인 시
		List<MyMenuDto> list = new ArrayList<>();
		if (memberId == null) {
			// 미로그인 역할(ROLE_ANONYMOUS)과 매핑된 메뉴 조회
			list = cmmtMenuDao.selectList_roleId(systemId, CodeExt.role.미로그인);
		}
		else {
			// 회원에 부여된 권한ID를 이용하여 역할과 매핑된 메뉴 조회
			// (권한ID는 변경사항을 바로 적용하기 위해서 Token이 아닌 CMMV_USER 테이블에서 직접 조회)
			CmmvUser user = cmmvUserDao.select(memberId);
			list = cmmtMenuDao.selectList_authorityId(systemId, user.getAuthorityId());
		}

		return new JsonList<>(list);
	}

	/**
	 * 메뉴 정렬
	 * @param systemId
	 * @param list
	 */
	public void orderMenu(String systemId, List<MenuOrderRequestDto> list) {
		BnMember worker = SecurityUtils.checkWorkerIsInsider();
		Date now = new Date();
		
		for(int i = 0; i < list.size(); i++) {
			CmmtMenu menu = CmmtMenu.builder()
					.sortOrder((long) (i + 1))
					.updatedDt(now)
					.updaterId(worker.getMemberId())
					.systemId(systemId)
					.menuId(list.get(i).getMenuId())
					.parentMenuId(list.get(i).getParentMenuId())
					.build();
			
			// 변경된 정렬순번 저장
			cmmtMenuDao.updateSortOrder(menu);
		}
	}
}