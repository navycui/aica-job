package aicluster.framework.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.CmmtAtchmnfl;

@Repository("FwCmmtAtchmnflDao")
public interface CmmtAtchmnflDao {
	void insertList(@Param("list") List<CmmtAtchmnfl> list);

	CmmtAtchmnfl select(String attachmentId);

	void increaseDownloadCnt(String attachmentId);

	List<CmmtAtchmnfl> selectList(String attachmentGroupId);

	List<CmmtAtchmnfl> selectList_notDeleted(String attachmentGroupId);

	void delete(String attachmentId);

	boolean existsGroupFiles(String attachmentGroupId);

	void updateRemoved_group(String attachmentGroupId);

	void updateRemoved(String attachmentId);

	long selectGroupFileSize(String attachmentGroupId);
}
