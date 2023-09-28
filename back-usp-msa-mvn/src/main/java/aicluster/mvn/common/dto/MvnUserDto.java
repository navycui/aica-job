package aicluster.mvn.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnUserDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8750020298244635540L;

    private String cmpnyId;
    private String cmpnyNm;
    private String bizrno;
    private String ceoNm;
    private String chargerNm;
    @JsonIgnore
    private String encChargerMobileNo;
    @JsonIgnore
    private String encChargerEmail;

    public String getChargerMobileNo() {
        if (string.isBlank(this.encChargerMobileNo)) {
        	return null;
        }
        return aes256.decrypt(this.encChargerMobileNo, this.cmpnyId);
    }

    public String getChargerEmail() {
    	if (string.isBlank(this.encChargerEmail)) {
    		return null;
    	}
    	return aes256.decrypt(this.encChargerEmail, this.cmpnyId);
    }
}
