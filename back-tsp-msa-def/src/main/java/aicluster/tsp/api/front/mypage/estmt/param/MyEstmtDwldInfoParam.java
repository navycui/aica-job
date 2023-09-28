package aicluster.tsp.api.front.mypage.estmt.param;

import bnet.library.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "견적서 다운로드 정보")
public class MyEstmtDwldInfoParam implements Serializable {
    private static final long serialVersionUID = 1891053257228496402L;
    @JsonIgnore
    @ApiModelProperty(value = "담당자ID")
    private String mberId;
    @ApiModelProperty(value = "담당자 로그인 ID")
    private String loginId;
    @ApiModelProperty(value = "담당자명")
    private String mberNm;
    @JsonIgnore
    @ApiModelProperty(value = "담당자 전화번호")
    private String encptTelno;
    @JsonIgnore
    @ApiModelProperty(value = "담당자 Email")
    private String encptEmail;
    @ApiModelProperty(value = "담당자 직급")
    private String clsfNm;


    public String getTelno() {
        if (CoreUtils.string.isBlank(this.encptTelno)) {
            return null;
        }
        return CoreUtils.aes256.decrypt(this.encptTelno, this.mberId);
    }

    public String getEmail() {
        if (CoreUtils.string.isBlank(this.encptEmail)) {
            return null;
        }
        return CoreUtils.aes256.decrypt(this.encptEmail, this.mberId);
    }


}
