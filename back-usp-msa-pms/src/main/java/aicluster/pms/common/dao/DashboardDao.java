package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.dto.DashboardDto;

@Repository
public interface DashboardDao {

	/** 사업신청 */
	public DashboardDto selectBsnsApply(String insiderId);
	/** 선정관리 */
	public DashboardDto selectSlctnWait(String insiderId);
	/** 이의신청 */
	public DashboardDto selectObjcReqst(String insiderId);
	/** 과제관리 */
	public DashboardDto selectReprt(String insiderId);
	/** 성과관리 */
	public DashboardDto selectPfmc(String insiderId);
	/** 교육관리 */
	public DashboardDto selectEduWait(String insiderId);
	/** 평가관리 */
	public DashboardDto selectEvl(String insiderId);
	/** 사업계획서 제출 */
	public DashboardDto selectBsnsWtpln(String insiderId);
	/** 사업협약서명 */
	public DashboardDto selectBsnsCnvnSign(String insiderId);
	/** 사업협약변경 */
	public DashboardDto selectBsnsCnvnChg(String insiderId);
	/** 전문가신청 */
	public DashboardDto selectExpert();
}
