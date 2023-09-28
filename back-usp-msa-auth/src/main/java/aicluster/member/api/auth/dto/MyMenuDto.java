package aicluster.member.api.auth.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMenuDto implements Serializable {

	private static final long serialVersionUID = 883727728531908575L;

	private String menuId;			/** 메뉴ID */
	private String menuNm;			/** 메뉴이름 */
	private String url;				/** URL */
	private Boolean newWindow;		/** 새창여부 */
	private String parentMenuId;	/** 부모메뉴 ID */
	private Long sortOrder;			/** 정렬순서 */
	private boolean readYn;			/** 조회권한여부 */
}
