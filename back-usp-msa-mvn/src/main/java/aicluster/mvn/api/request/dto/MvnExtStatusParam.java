package aicluster.mvn.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnExtStatusParam implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5198420367178157086L;
    private String mvnEtReqId;
    private String mvnEtReqSt;
    private String makeupReqCn;
}
