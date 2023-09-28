package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.resource.dto.AlrsrcCmpnyListParam;
import aicluster.mvn.api.resource.dto.AlrsrcStatusParam;
import aicluster.mvn.api.resource.dto.AlrsrcUserListParam;
import aicluster.mvn.common.dto.AlrsrcCmpnyListItemDto;
import aicluster.mvn.common.dto.AlrsrcCmpnySlctnDto;
import aicluster.mvn.common.dto.AlrsrcStatusListItemDto;
import aicluster.mvn.common.dto.AlrsrcUserListItemDto;
import aicluster.mvn.common.dto.AlrsrcUserDto;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;

@Repository
public interface UsptResrceAsgnEntrpsDao {
	void insertList(List<UsptResrceAsgnEntrps> list);
	UsptResrceAsgnEntrps select(String alrsrcId);
	void update(UsptResrceAsgnEntrps usptAlrsrcCmpny);
	Long selectList_count(
			@Param("param") AlrsrcCmpnyListParam param
			);
	List<AlrsrcCmpnyListItemDto> selectList(
			@Param("param") AlrsrcCmpnyListParam param,     /* 검색조건      */
			@Param("beginRowNum") Long beginRowNum,         /* 시작 Row 위치 */
			@Param("itemsPerPage") Long itemsPerPage,       /* 페이지        */
			@Param("totalItems") Long totalItems            /* 전체 건수     */
			);
	Long selectStatusList_count(
			@Param("param") AlrsrcStatusParam param
			);
	List<AlrsrcStatusListItemDto> selectStatusList(
			@Param("param") AlrsrcStatusParam param,     /* 검색조건      */
			@Param("beginRowNum") Long beginRowNum,              /* 시작 Row 위치 */
			@Param("itemsPerPage") Long itemsPerPage,            /* 페이지        */
			@Param("totalItems") Long totalItems                 /* 전체 건수     */
			);
	Long selectUserList_count(
			@Param("param") AlrsrcUserListParam param
			);
	List<AlrsrcUserListItemDto> selectUserList(
			@Param("param") AlrsrcUserListParam param,      /* 검색조건      */
			@Param("beginRowNum") Long beginRowNum,              /* 시작 Row 위치 */
			@Param("itemsPerPage") Long itemsPerPage,            /* 페이지        */
			@Param("totalItems") Long totalItems                 /* 전체 건수     */
			);
	AlrsrcUserDto selectUser(
			@Param("cmpnyId") String cmpnyId,            /* 업체ID(CMMV_MEMBER.MEMBER_ID) */
			@Param("alrsrcId") String alrsrcId           /* 자원할당ID                    */
			);
	List<AlrsrcCmpnySlctnDto> selectList_slctnId(String evlLastSlctnId);
}
