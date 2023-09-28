package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertCl;

@Repository
public interface UsptExpertClDao {

	/**
	 *전문가분류
	 */


	/*전문가단 트리 0depth 코드 조회*/
	String selectExpertClfcOneTree();
	/*전문가단 트리 1depth 코드목록 조회*/
	List<UsptExpertCl> selectExpertClfcTwoTreeList();
	/*max sort order 조회*/
	int selectExpertClfcSortOrderMax();


	/*전문가단 소속트리 나의목록 조회*/
	List<UsptExpertCl> selectExpertClfcMyTreeList(String expertClId);

	/*전문가단 등록*/
	int insertExpertCl(UsptExpertCl inputParam);
	/*전문가단 수정*/
	int updateExpertCl(UsptExpertCl inputParam);
	/*전문가분류명 수정*/
	int updateExpertClNm(UsptExpertCl inputParam);
	/*전문가단 삭제*/
	int deleteExpertCl(UsptExpertCl inputParam);
	/*전문가단 부모ID 삭제*/
	int deleteExpertClParnts(UsptExpertCl inputParam);

	/*전문가분류 조회(다음단계 목록)*/
	List<UsptExpertCl> selectExpertClIdList(String expertClId);
	/*전문가분류 조회(다음단계 목록)*/
	List<UsptExpertCl> selectExpertClIdInfoList(String expertClId);
	/*1depth ID조회*/
	String selectExpertClOneDepth(String expertClId);
}
