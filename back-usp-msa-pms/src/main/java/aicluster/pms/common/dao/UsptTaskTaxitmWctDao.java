package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptTaskTaxitmWct;

@Repository
public interface UsptTaskTaxitmWctDao {

	int insert(UsptTaskTaxitmWct usptTaskTaxitmWct);

	int update(UsptTaskTaxitmWct usptTaskTaxitmWct);

	List<UsptTaskTaxitmWct> selectBsnsPlanTaxitmWctList(UsptTaskTaxitmWct usptTaskTaxitmWct);

	List <String> selectBsnsPlanTaxitmWctBsnsYearList(UsptTaskTaxitmWct usptTaskTaxitmWct);

	int deleteBsnsPlanDocIdAll(String bsnsPlanDocId);



}
