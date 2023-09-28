package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPlanProcessHist;

@Repository
public interface UsptBsnsPlanProcessHistDao {

	int delete(UsptBsnsPlanProcessHist usptBsnsPlanProcessHist);

	int insert(UsptBsnsPlanProcessHist usptBsnsPlanProcessHist);

	int update(UsptBsnsPlanProcessHist usptBsnsPlanProcessHist);

	List<UsptBsnsPlanProcessHist> selectList(UsptBsnsPlanProcessHist usptBsnsPlanProcessHist);

}
