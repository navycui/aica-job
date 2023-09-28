package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsCl;

@Repository
public interface UsptBsnsClDao {

	List<UsptBsnsCl> selectList(@Param("parentBsnsClId") String parentBsnsClId, @Param("enabled") String enabled);
	void insert(UsptBsnsCl bcInfo);
	int update(UsptBsnsCl bcInfo);
	int delete(String bsnsClId);
	/**
	 * 산업분류 사용건수 조회
	 * @param bcInfo
	 * @return
	 */
	Integer selectUseCnt(String bsnsClId);


	/**
	 * 산업분류명 조회
	 * @param bsnsClId
	 * @return
	 */
	String selectBsnsClNm(String bsnsClId);


	/**
	 * 하위분류 삭제
	 * @param bsnsClId
	 * @return
	 */
	int deleteAllForChild(String bsnsClId);
}
