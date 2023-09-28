package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtBbsAuthor implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8936405184003570262L;
	private String boardId;
	private String authorityId;
	private String boardAuthority;
	@JsonIgnore
	private String creatorId;
	@JsonIgnore
	private Date createdDt;

	private String authorityNm;
	private String boardAuthorityNm;
	
	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
		}
		return null;
	}
	
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
}
