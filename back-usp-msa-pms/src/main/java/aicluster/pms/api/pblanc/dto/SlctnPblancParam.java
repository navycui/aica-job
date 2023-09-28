package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptSlctnPblanc;
import aicluster.pms.common.entity.UsptSlctnPblancDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SlctnPblancParam extends UsptSlctnPblanc implements Serializable{
	private static final long serialVersionUID = 3910973930000386673L;
	List<UsptSlctnPblancDetail> detailList;
	List<CmmtAtchmnfl> deleteFileList;
}
