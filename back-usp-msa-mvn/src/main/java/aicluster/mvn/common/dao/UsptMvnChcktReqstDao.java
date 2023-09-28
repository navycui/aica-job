package aicluster.mvn.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.mvn.api.request.dto.MvnCheckoutListParam;
import aicluster.mvn.common.dto.CheckoutReqListItemDto;
import aicluster.mvn.common.dto.CheckoutReqStCountDto;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;

@Repository
public interface UsptMvnChcktReqstDao {
	void insert(UsptMvnChcktReqst usptCheckoutReq);

	UsptMvnChcktReqst select(String checkoutReqId);

	void update(UsptMvnChcktReqst usptCheckoutReq);

	Long selectList_count( @Param("param") MvnCheckoutListParam param );

	List<CheckoutReqListItemDto> selectList(
			@Param("param") MvnCheckoutListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	UsptMvnChcktReqst select_cmpnyId(
			@Param("mvnId") String mvnId,
			@Param("cmpnyId") String cmpnyId);

	UsptMvnChcktReqst select_mvnId(String mvnId);

	List<UsptMvnChcktReqst> selectTarget_emptyFc();

	CheckoutReqStCountDto selectChekcoutReqSt_count();
}
