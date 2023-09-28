package aicluster.common.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerersListItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8319234166936603622L;

    private String memberId;
    private String memberNm;
    private String loginId;
    private String memberType;
    private String memberTypeNm;
    private String authorityId;
    @JsonIgnore
    private String encEmail;
    @JsonIgnore
    private String encMobileNo;

    public String getEmail() {
        if (string.isBlank(this.encEmail)) {
            return null;
        }
        return masking.maskingEmail(aes256.decrypt(this.encEmail, this.memberId));
    }

    public String getMobileNo() {
        if (string.isBlank(this.encMobileNo)) {
            return null;
        }
        return masking.maskingMobileNo(aes256.decrypt(this.encMobileNo, this.memberId));
    }
}
