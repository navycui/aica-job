package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class HistDtListItemDto implements Serializable {

	private static final long serialVersionUID = 875063779623603613L;

	private Date  histDt;			       /** 이력일시 */

    public String getFmtHistDt() {
        if (this.histDt == null) {
            return null;
        }
        return date.format(this.histDt, "yyyy-MM-dd");
    }
}
