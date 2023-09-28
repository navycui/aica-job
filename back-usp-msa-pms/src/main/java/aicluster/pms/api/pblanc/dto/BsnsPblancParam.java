package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsPblanc;
import aicluster.pms.common.entity.UsptBsnsPblancAppnTask;
import aicluster.pms.common.entity.UsptBsnsPblancDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BsnsPblancParam extends UsptBsnsPblanc implements Serializable {
	private static final long serialVersionUID = 3761553143276198356L;
	List<UsptBsnsPblancDetail> pblancDetailList;
	List<UsptBsnsPblancAppnTask> taskList;
	List<CmmtAtchmnfl> deleteFileList;
}
