package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RsltStatsDto  implements Serializable{
	private static final long serialVersionUID = -1144467183093887288L;
	/** 요약항목 목록 */
	List<String> sumryIemList;
	/** 요약항목 내용 */
	List<Long> sumryIemCnList;
	/** 목록 항목 목록 */
	List<String> listIemList;
	/** 목록 */
	List<List<String>> list;
}
