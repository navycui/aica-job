package aicluster.batch.common.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LmsEduCateT implements Serializable {
	private static final long serialVersionUID = -231809195585200257L;
	private String cateId;
	private String gtypeCd;
	private String title;
	private String bFileId;
	private String sFileId;
	private String cateDesc;
	private String cateCpse;
	private String cateCpseDesc;
	private String orderCd;
}
