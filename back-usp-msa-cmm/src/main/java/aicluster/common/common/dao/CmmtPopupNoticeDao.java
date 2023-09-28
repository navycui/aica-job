package aicluster.common.common.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.popup.dto.PopupGetListParam;
import aicluster.common.common.dto.PopupListItem;
import aicluster.common.common.entity.CmmtPopupNotice;

@Repository
public interface CmmtPopupNoticeDao {

	long selectList_count(@Param("param") PopupGetListParam param);

	List<PopupListItem> selectList(
			@Param("param") PopupGetListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

	void insert(CmmtPopupNotice popup);

	CmmtPopupNotice select(String popupId);

	void update(CmmtPopupNotice popup);

	void delete(String popupId);

	List<CmmtPopupNotice> selectList_time(
			@Param("systemId") String systemId,
			@Param("dtime") Date dtime);

}
