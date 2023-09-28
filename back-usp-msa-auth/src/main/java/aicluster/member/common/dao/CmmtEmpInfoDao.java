package aicluster.member.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.member.api.insider.dto.InsiderListParam;
import aicluster.member.api.insider.dto.SrchPicItemDto;
import aicluster.member.api.insider.dto.SrchPicParam;
import aicluster.member.common.dto.InsiderDto;
import aicluster.member.common.entity.CmmtEmpInfo;

@Repository
public interface CmmtEmpInfoDao {
	CmmtEmpInfo selectByLoginId(String loginId);
	CmmtEmpInfo select(String memberId);
	CmmtEmpInfo selectByRefreshToken(String refreshToken);
	long selectCountByAuthorityId(String authorityId);
	long selectCount(
			@Param("param") InsiderListParam param
			);
	List<InsiderDto> selectList(
			@Param("param") InsiderListParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage);
	void insert(CmmtEmpInfo insider);
	void update(CmmtEmpInfo insider);
	Long selectSrchPicCount(
			@Param("param") SrchPicParam param);
	List<SrchPicItemDto> selectSrchPicList(
			@Param("param") SrchPicParam param,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage);
}
