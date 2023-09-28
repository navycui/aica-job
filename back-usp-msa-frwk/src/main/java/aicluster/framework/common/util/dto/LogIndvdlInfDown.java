package aicluster.framework.common.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogIndvdlInfDown implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6271620362632199477L;

	private String memberId;		/** 회원ID */
	private String memberIp;		/** 회원IP */
	private String workTypeNm;		/** 작업구분명 */
	private String workCn;			/** 작업내용 */
	private String menuId;			/** 메뉴ID */
	private String menuNm;			/** 메뉴명 */
	private String fileNm;			/** 다운로드 파일명 */

	private List<LogIndvdInfTrgtItem> trgtIdList;	/** 대상자 목록 */
	
	public List<LogIndvdInfTrgtItem> getTrgtIdList() {
		List<LogIndvdInfTrgtItem> trgtIdList = new ArrayList<>();
		if(this.trgtIdList != null) {
			trgtIdList.addAll(this.trgtIdList);
		}
		return trgtIdList;
	}
	
	public void setTrgtIdList(List<LogIndvdInfTrgtItem> trgtIdList) {
		this.trgtIdList = new ArrayList<>();
		if(trgtIdList != null) {
			this.trgtIdList.addAll(trgtIdList);
		}
	}
}
