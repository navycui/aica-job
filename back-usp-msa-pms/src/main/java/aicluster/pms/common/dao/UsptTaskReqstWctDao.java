package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptTaskReqstWct;

@Repository
public interface UsptTaskReqstWctDao {
	int delete(UsptTaskReqstWct usptTaskReqstWct);

	int insert(UsptTaskReqstWct usptTaskReqstWct);

	int update(UsptTaskReqstWct usptTaskReqstWct);

	List<UsptTaskReqstWct> selectList(UsptTaskReqstWct usptTaskReqstWct);

	UsptTaskReqstWct selectOne(UsptTaskReqstWct usptTaskReqstWct);

	List<UsptTaskReqstWct> selectBsnsYearList(String  bsnsPlanDocId);

	int deleteBsnsPlanDocIdAll(String bsnsPlanDocId);

}