package aicluster.common.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.common.api.banner.dto.BannerGetListParam;
import aicluster.common.common.entity.CmmtBanner;

@Repository
public interface CmmtBannerDao {

	CmmtBanner select(String bannerId);

	void update(CmmtBanner cmmtBanner);

	void insert(CmmtBanner cmmtBanner);

	void delete(String bannerId);

	List<CmmtBanner> selectList_today(String systemId);

	Long selectList_count(@Param("param") BannerGetListParam param);

	List<CmmtBanner> selectList(
			@Param("param") BannerGetListParam param,
			@Param("beginRowNum")Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage,
			@Param("totalItems") Long totalItems);

}
